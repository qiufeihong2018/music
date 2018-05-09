package cn.tedu.tedu_player_1;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.R.bool;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// ǰһ��ʱ�䣬��һ��ʱ��
	TextView txt1, txt2;
	// ���Ž�����
	private static SeekBar seekBar;
	// ImageButton������/��ͣ
	private ImageButton ibPlayOrPause;
	// ImageButton����һ��
	private ImageButton ibPrevious;
	// ImageButton����һ��
	private ImageButton ibNext;

	// ListView�������б�
	private ListView lvMusics;
	// �������ݵ�List����
	private List<Music> musics;
	// Adapter
	private MusicAdapter adapter;
	// ���Ź���
	private MediaPlayer player;
	// ��ͣʱ���ŵ���λ��
	private int pausePosition;
	// ��ǰ���ڲ��ŵĸ���������
	private int currentMusicIndex;

	// ����������Ĺ���
	private Random random = new Random();
	// //������
	// private int songNum;
	// private int flag;
	//
	//
	// //����ģʽ
	private ImageButton Play_style;

	// private int Xun=1;
	// private int Sui=2;
	// private int Dan=3;
	//

	// private MusicHandler musicHandler;// ����ı�������¼�
	// private MusicThread musicThread;// �Զ��ı���������߳�
	// private boolean autoChange, manulChange;// �ж��ǽ��������Զ��ı仹���ֶ��ı�
	// private boolean isPause;// �ж��Ǵ���ͣ�лָ��������²���
	//
	//
	// public class MySeekBarListener implements OnSeekBarChangeListener{
	//
	// @Override
	// public void onProgressChanged(SeekBar seekBar, int progress, boolean
	// fromUser) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onStartTrackingTouch(SeekBar seekBar) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// public void onStopTrackingTouch(SeekBar seekBar) { // ֹͣ�϶�
	// int progress = seekBar.getProgress(); //��õĵ�ǰ����
	//
	// if (!autoChange && manulChange) { //������ֶ�
	// int musicMax = player.getDuration(); //�õ����׸��������
	// int seekBarMax = seekBar.getMax(); //�õ������������ֵ
	//
	// player.seekTo(musicMax * progress / seekBarMax);//������������ =��������*��ǰ����/��������
	// pause(); //��ͣ
	// autoChange = true;
	// manulChange = false;
	// }
	// }
	//
	//
	// }
	//
	//
	// class MusicHandler extends Handler {
	//
	// public MusicHandler() {
	// }
	//
	// @Override
	// public void handleMessage(Message msg) {
	// if (autoChange) {
	// try {
	// int position = player.getCurrentPosition();//�õ���ǰ�������Ž���(��)
	// int mMax = player.getDuration();//������ʱ��
	// int sMax = seekBar.getMax();//�������������ֵ����ٷֱȣ�
	// seekBar.setProgress(position * sMax / mMax);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// seekBar.setProgress(0);
	//
	// }
	// }
	// }
	//
	// class MusicThread implements Runnable{
	//
	// @Override
	// public void run() {
	// while(true){
	// try{
	// musicHandler.sendMessage(new Message());
	// Thread.sleep(1000);
	//
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }
	//
	// }
	//
	//
	// }
	//
	// //�л������
	// public void chageToSui(){
	//
	//
	// }
	//
	// //�л�������
	//
	// public void chageToDan(){
	//
	//
	// }
	//
	// //�л���ѭ��
	// public void chageToXun(){
	//
	//
	// }
	//
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ���ؼ�
		lvMusics = (ListView) findViewById(R.id.lv_musics);
		ibPlayOrPause = (ImageButton) findViewById(R.id.ib_play_or_pause);
		ibPrevious = (ImageButton) findViewById(R.id.ib_previous);
		ibNext = (ImageButton) findViewById(R.id.ib_next);
		Play_style = (ImageButton) findViewById(R.id.play_style);
		txt1 = (TextView) this.findViewById(R.id.left_bar);
		txt2 = (TextView) this.findViewById(R.id.right_bar);
		seekBar = (SeekBar) this.findViewById(R.id.seekBar);
		// seekBar.setOnSeekBarChangeListener(new MySeekBarListener());

		// ��ȡ����������
		loadData();

		// �������Ź���
		player = new MediaPlayer();

		// ΪMediaPlayer���ò������ʱ������
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				next();
			}
		});

		// ����Adapter
		adapter = new MusicAdapter(this, musics);

		// ΪListView����Adapter
		lvMusics.setAdapter(adapter);

		// ������ť����ļ�������Ķ���
		InnerOnClickListener listener = new InnerOnClickListener();

		// Ϊ��ť���ü�����
		ibPlayOrPause.setOnClickListener(listener);
		ibPrevious.setOnClickListener(listener);
		ibNext.setOnClickListener(listener);
		Play_style.setOnClickListener(listener);// ������ǧ�����

		//
		//
		// ���������б�ListView���б������ļ�������Ķ���
		InnerOnItemClickListener listener2 = new InnerOnItemClickListener();

		// Ϊ�����б�ListView���ü�����
		lvMusics.setOnItemClickListener(listener2);

		InnerOnSeekBarChangeListener listener3 = new InnerOnSeekBarChangeListener();

		seekBar.setOnSeekBarChangeListener(listener3);

		isAppFirstRunning = true;
	}

	// ����app�Ƿ�ոտ�ʼ���У��ѽ����һ�ε���б��һ��������ŵ�����
	private boolean isAppFirstRunning;
	// ���½��ȵ��߳�
	private UpdateProgressThread thread;

	private class UpdateProgressThread extends Thread {
		boolean isRunning;

		public void run() {
			Runnable runner = new Runnable() {

				@Override
				public void run() {
					int currentPosition = player.getCurrentPosition();
					int duration = player.getDuration();
					int progress = currentPosition * 100 / duration;
					if (!isTrackingTouch) {
						seekBar.setProgress(progress);
					}
					txt1.setText(getFormattedTime(currentPosition));
				}
			};
			while (isRunning) {
				if (player.isPlaying()) {
					runOnUiThread(runner);
					Log.d("�ṷ", "���ڸ��²��Ž���");

				} else {
					Log.w("�ṷ", "û�����ڲ��Ÿ��������ظ��²��Ž��ȡ���");
				}
				try {
					Thread.sleep(1000);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void startUpdateProgressThread() {
		if (thread == null) {
			thread = new UpdateProgressThread();
			thread.isRunning = true;
			thread.start();
		}

	}

	private void stopUpdateProgressThread() {
		if (thread != null) {
			thread.isRunning = false;
			thread = null;
		}

	}

	// Locale ��ʾ������ÿһ��Locale���󶼴�����һ���ض��ĵ������κ��Ļ�������
	private SimpleDateFormat ss = new SimpleDateFormat("mm:ss", Locale.CHINA);
	private Date date = new Date();

	private String getFormattedTime(long millis) {
		date.setTime(millis);
		return ss.format(date);
	}

	private boolean isTrackingTouch;

	private class InnerOnSeekBarChangeListener implements
			OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			isTrackingTouch = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			isTrackingTouch = false;
			seekTo(seekBar.getProgress());
		}
	}

	// �������Ƿ�ʼ����
	private boolean isPlayWorking;

	// ��ָ���Ľ���ֵ��ʼ����
	private void seekTo(int progress) {
		// �жϵ�ǰ�Ƿ���ع��κθ���
		if (isPlayWorking) {
			// ������Ҫ��ĳʱ��㿪ʼ����
			int millis = player.getDuration() * progress / 100;
			// �������ֵ���и���
			pausePosition = millis;
			play();

		}
	}

	// private void play(int position){
	// if(isAppFirstRunning||position!=currentMusicIndex){
	// currentMusicIndex=position;
	// pausePosition=0;
	// play();
	//
	// }
	// }
	// ʵ�ָ����б�ListView���б������ļ�������
	private class InnerOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// ����ָ��λ�õĸ���
			play(position);
		}

	}

	// ʵ�ְ�ť����ļ�������
	private class InnerOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// �жϵ���İ�ť
			switch (v.getId()) {
			case R.id.ib_play_or_pause:
				// �жϵ�ǰ�Ĳ���״̬
				if (player.isPlaying()) {
					// ���ڲ��ţ�����ͣ
					pause();
				} else {
					// û���ڲ��ţ��򲥷�
					play();
				}
				break;

			case R.id.ib_previous:
				// ������һ��
				previous();
				break;

			case R.id.ib_next:
				// ������һ��
				next();
				break;

			case R.id.play_style:
				// �л�����ģʽ���б�ѭ�� ѭ������ �������
				mode++;
				mode %= modes.length;
				Play_style.setImageResource(modes[mode]);
				break;
			// int i=0;
			// i++;
			// if(i%3==1){
			// Toast.makeText(getApplicationContext(),"�������",
			// Toast.LENGTH_SHORT).show();
			// //���������
			// songNum=(int)(Math.random()*musics.size());
			// System.out.println("song---"+songNum);
			//
			// //�������flag
			// flag=1;
			// play_style.setBackgroundResource(R.drawable.ic_mode_random_pressed);
			// }
			// if(i%3==2){
			// Toast.makeText(getApplicationContext(),"��������",
			// Toast.LENGTH_SHORT).show();
			// flag=2;
			// play_style.setBackgroundResource(R.drawable.ic_mode_single_pressed);
			//
			// }
			// else{
			// Toast.makeText(getApplicationContext(), "ѭ������",
			// Toast.LENGTH_SHORT).show();
			// flag=0;
			// play_style.setBackgroundResource(R.drawable.ic_mode_random_pressed);
			// }
			// break;
			// default:
			// break;
			//
			}
		}
	}

	// ��ǰ�Ĳ���ģʽ
	private int mode;
	// ����ģʽ���б�ѭ��,����ѭ�����������
	private static final int mode_repeat = 0;
	private static final int mode_single = 1;
	private static final int mode_random = 2;

	// ����ģʽ
	private int[] modes = {

	R.drawable.button_mode_repeat, R.drawable.button_mode_single,
			R.drawable.button_mode_random

	};

	// ������һ��
	private void next() {
		switch (mode) {

		case mode_repeat:
			currentMusicIndex++;
			if (currentMusicIndex >= musics.size()) {
				currentMusicIndex = 0;
			}
			break;

		case mode_single:
			break;

		case mode_random:
			// �����������������Ҫ���ŵĸ�������
			int i = random.nextInt(musics.size());
			// �жϸ��������Ƿ����1�������û�б�Ҫ��������
			if (musics.size() > 1) {
				// ������ɵ���������ǵ�ǰ�����������򷴸�����,ֱ�����ɲ�ͬ������
				while (i == currentMusicIndex) {
					i = random.nextInt(musics.size());
				}
			}
			currentMusicIndex = i;
			break;

		}
		// ��ռ�¼����ͣ��λ��
		pausePosition = 0;
		// ����
		play();
	}

	// ������һ��
	private void previous() {

		switch (mode) {
		case mode_repeat:
			currentMusicIndex--;
			if (currentMusicIndex < 0) {

				currentMusicIndex = musics.size() - 1;
			}

			break;

		case mode_single:
			break;
			
		case mode_random:
			int i = random.nextInt(musics.size());
			if (musics.size() > 1) {

				while (i == currentMusicIndex) {
					i = random.nextInt(musics.size());

				}

			}
			currentMusicIndex=i;
			break;
		}

		// ��ռ�¼����ͣ��λ��
		pausePosition = 0;
		// ����
		play();
	}

	// ��ͣ����
	private void pause() {
		// 1. ִ����ͣ
		player.pause();
		// 2. ��¼����ͣʱ���ŵ���λ��
		pausePosition = player.getCurrentPosition();
		// 3. ����ť��ͼƬ����Ϊ�����š�
		ibPlayOrPause.setImageResource(R.drawable.selector_button_play);
		// 4.ֹͣ���½��ȵ��߳�
		stopUpdateProgressThread();

	}

	// ����ָ��λ�õĸ���
	private void play(int position) {
		// �жϵ�����Ƿ��ǵ�ǰ���ڲ��ŵ�
		if (isAppFirstRunning || position != currentMusicIndex) {
			// ������һ�׸���������
			currentMusicIndex = position;
			// ��ռ�¼����ͣ��λ��
			pausePosition = 0;
			// ����
			play();
		}
	}

	// ���Ÿ���
	private void play() {
		try {
			// 1. ����
			player.reset();
			// 2. ������Ҫ���ŵĸ���
			player.setDataSource(musics.get(currentMusicIndex).getPath());
			// 3. ����
			player.prepare();
			// 4. ���
			player.seekTo(pausePosition);
			// 5. ����
			player.start();
			// 6. ����ť��ͼƬ����Ϊ����ͣ��
			ibPlayOrPause.setImageResource(R.drawable.selector_button_pause);
			// 7. ����ǰ������ʶΪ���ڲ���
			for (int i = 0; i < musics.size(); i++) {
				musics.get(i).setPlaying(false);
			}
			musics.get(currentMusicIndex).setPlaying(true);
			adapter.notifyDataSetChanged();// ͨ��һ���ⲿ�ķ�����
			// ����������������ݸı�ʱ��Ҫǿ�Ƶ���getview��
			// ˢ��ÿ��item�����ݣ�����ʵ�ֶ�̬��ˢ���б�Ĺ��ܡ�
			// 8. ����ListView ���ƻ����ٶ�
			lvMusics.smoothScrollToPosition(currentMusicIndex);
			// 9��ʶΪ���Ǹոմ�Ӧ�ó���
			isAppFirstRunning = false;
			// ��ʾ��ǰ������ʱ��
			int duration = player.getDuration();
			txt2.setText(getFormattedTime(duration));
			// 11�������������½��ȵ��߳�
			startUpdateProgressThread();
			// 12��ʶΪ�������Ѿ���ʼ���������ڽ��û�в����κθ���ʱ��ҷ���������ֵ�����
			isPlayWorking = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ��ȡ����������
	private void loadData() {
		// ���������б��List����
		musics = new ArrayList<Music>();
		// ���SDCARD�Ƿ����
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// ȷ����Ÿ����ļ����ļ���(sdcard�µ�Music�ļ�)
			File musicDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			// ���Music�ļ����Ƿ����
			if (musicDir.exists()) {
				// ��ȡ���ļ����µ�����File����
				File[] files = musicDir.listFiles();
				// ���File�����Ƿ���Ч
				if (files != null && files.length > 0) {
					// ����File����
					for (int i = 0; i < files.length; i++) {
						// �ж��Ƿ����ļ�
						if (files[i].isFile()) {
							// �ж��Ƿ���.mp3�ļ� ��touppercase()ת��Ϊ��д����MP3��β��
							String fileName = files[i].getName();
							if (fileName.toUpperCase(Locale.CHINA).endsWith(
									".MP3")) {
								// ����Music���� ���þ���·��
								Music music = new Music();
								music.setTitle(fileName.substring(0,
										fileName.length() - 4));
								music.setPath(files[i].getAbsolutePath());
								// ��־
								Log.v("tedu", "ɨ�赽������" + music);
								// ��Music������ӵ�������
								musics.add(music);
							}
						}
					}
				}
			}
		}
	}
}
