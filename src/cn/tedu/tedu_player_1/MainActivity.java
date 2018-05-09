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
	// 前一个时间，后一个时间
	TextView txt1, txt2;
	// 播放进度条
	private static SeekBar seekBar;
	// ImageButton：播放/暂停
	private ImageButton ibPlayOrPause;
	// ImageButton：上一首
	private ImageButton ibPrevious;
	// ImageButton：下一首
	private ImageButton ibNext;

	// ListView：歌曲列表
	private ListView lvMusics;
	// 歌曲数据的List集合
	private List<Music> musics;
	// Adapter
	private MusicAdapter adapter;
	// 播放工具
	private MediaPlayer player;
	// 暂停时播放到的位置
	private int pausePosition;
	// 当前正在播放的歌曲的索引
	private int currentMusicIndex;

	// 生成随机数的工具
	private Random random = new Random();
	// //歌曲数
	// private int songNum;
	// private int flag;
	//
	//
	// //播放模式
	private ImageButton Play_style;

	// private int Xun=1;
	// private int Sui=2;
	// private int Dan=3;
	//

	// private MusicHandler musicHandler;// 处理改变进度条事件
	// private MusicThread musicThread;// 自动改变进度条的线程
	// private boolean autoChange, manulChange;// 判断是进度条是自动改变还是手动改变
	// private boolean isPause;// 判断是从暂停中恢复还是重新播放
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
	// public void onStopTrackingTouch(SeekBar seekBar) { // 停止拖动
	// int progress = seekBar.getProgress(); //获得的当前进度
	//
	// if (!autoChange && manulChange) { //如果是手动
	// int musicMax = player.getDuration(); //得到该首歌曲最长秒数
	// int seekBarMax = seekBar.getMax(); //得到进度条的最大值
	//
	// player.seekTo(musicMax * progress / seekBarMax);//跳到该曲该秒 =歌曲长度*当前进度/进度条长
	// pause(); //暂停
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
	// int position = player.getCurrentPosition();//得到当前歌曲播放进度(秒)
	// int mMax = player.getDuration();//歌曲的时长
	// int sMax = seekBar.getMax();//进度条长（最大值，算百分比）
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
	// //切换到随机
	// public void chageToSui(){
	//
	//
	// }
	//
	// //切换到单曲
	//
	// public void chageToDan(){
	//
	//
	// }
	//
	// //切换到循序
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
		// 初始化控件
		lvMusics = (ListView) findViewById(R.id.lv_musics);
		ibPlayOrPause = (ImageButton) findViewById(R.id.ib_play_or_pause);
		ibPrevious = (ImageButton) findViewById(R.id.ib_previous);
		ibNext = (ImageButton) findViewById(R.id.ib_next);
		Play_style = (ImageButton) findViewById(R.id.play_style);
		txt1 = (TextView) this.findViewById(R.id.left_bar);
		txt2 = (TextView) this.findViewById(R.id.right_bar);
		seekBar = (SeekBar) this.findViewById(R.id.seekBar);
		// seekBar.setOnSeekBarChangeListener(new MySeekBarListener());

		// 获取歌曲的数据
		loadData();

		// 创建播放工具
		player = new MediaPlayer();

		// 为MediaPlayer配置播放完成时监听器
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				next();
			}
		});

		// 创建Adapter
		adapter = new MusicAdapter(this, musics);

		// 为ListView配置Adapter
		lvMusics.setAdapter(adapter);

		// 创建按钮点击的监听器类的对象
		InnerOnClickListener listener = new InnerOnClickListener();

		// 为按钮配置监听器
		ibPlayOrPause.setOnClickListener(listener);
		ibPrevious.setOnClickListener(listener);
		ibNext.setOnClickListener(listener);
		Play_style.setOnClickListener(listener);// 监听器千万别忘

		//
		//
		// 创建歌曲列表ListView的列表项点击的监听器类的对象
		InnerOnItemClickListener listener2 = new InnerOnItemClickListener();

		// 为歌曲列表ListView配置监听器
		lvMusics.setOnItemClickListener(listener2);

		InnerOnSeekBarChangeListener listener3 = new InnerOnSeekBarChangeListener();

		seekBar.setOnSeekBarChangeListener(listener3);

		isAppFirstRunning = true;
	}

	// 整个app是否刚刚开始运行，已解决第一次点击列表第一项不正常播放的问题
	private boolean isAppFirstRunning;
	// 更新进度的线程
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
					Log.d("酷狗", "正在更新播放进度");

				} else {
					Log.w("酷狗", "没有正在播放歌曲，不必更新播放进度……");
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

	// Locale 表示地区。每一个Locale对象都代表了一个特定的地理、政治和文化地区。
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

	// 播放器是否开始工作
	private boolean isPlayWorking;

	// 从指定的进度值开始播放
	private void seekTo(int progress) {
		// 判断当前是否加载过任何歌曲
		if (isPlayWorking) {
			// 计算需要从某时间点开始播放
			int millis = player.getDuration() * progress / 100;
			// 将计算的值进行复制
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
	// 实现歌曲列表ListView的列表项点击的监听器类
	private class InnerOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 播放指定位置的歌曲
			play(position);
		}

	}

	// 实现按钮点击的监听器类
	private class InnerOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// 判断点击的按钮
			switch (v.getId()) {
			case R.id.ib_play_or_pause:
				// 判断当前的播放状态
				if (player.isPlaying()) {
					// 正在播放，则暂停
					pause();
				} else {
					// 没有在播放，则播放
					play();
				}
				break;

			case R.id.ib_previous:
				// 播放上一首
				previous();
				break;

			case R.id.ib_next:
				// 播放下一首
				next();
				break;

			case R.id.play_style:
				// 切换播放模式：列表循环 循环单曲 随机播放
				mode++;
				mode %= modes.length;
				Play_style.setImageResource(modes[mode]);
				break;
			// int i=0;
			// i++;
			// if(i%3==1){
			// Toast.makeText(getApplicationContext(),"随机播放",
			// Toast.LENGTH_SHORT).show();
			// //产生随机数
			// songNum=(int)(Math.random()*musics.size());
			// System.out.println("song---"+songNum);
			//
			// //随机播放flag
			// flag=1;
			// play_style.setBackgroundResource(R.drawable.ic_mode_random_pressed);
			// }
			// if(i%3==2){
			// Toast.makeText(getApplicationContext(),"单曲播放",
			// Toast.LENGTH_SHORT).show();
			// flag=2;
			// play_style.setBackgroundResource(R.drawable.ic_mode_single_pressed);
			//
			// }
			// else{
			// Toast.makeText(getApplicationContext(), "循环播放",
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

	// 当前的播放模式
	private int mode;
	// 播放模式：列表循环,单曲循环，随机播放
	private static final int mode_repeat = 0;
	private static final int mode_single = 1;
	private static final int mode_random = 2;

	// 播放模式
	private int[] modes = {

	R.drawable.button_mode_repeat, R.drawable.button_mode_single,
			R.drawable.button_mode_random

	};

	// 播放下一首
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
			// 生成随机索引，既需要播放的歌曲索引
			int i = random.nextInt(musics.size());
			// 判断歌曲数量是否大于1，否则就没有必要反复生成
			if (musics.size() > 1) {
				// 如果生成的随机数就是当前歌曲索引，则反复生成,直至生成不同的索引
				while (i == currentMusicIndex) {
					i = random.nextInt(musics.size());
				}
			}
			currentMusicIndex = i;
			break;

		}
		// 清空记录的暂停的位置
		pausePosition = 0;
		// 播放
		play();
	}

	// 播放上一首
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

		// 清空记录的暂停的位置
		pausePosition = 0;
		// 播放
		play();
	}

	// 暂停播放
	private void pause() {
		// 1. 执行暂停
		player.pause();
		// 2. 记录下暂停时播放到的位置
		pausePosition = player.getCurrentPosition();
		// 3. 将按钮的图片设置为“播放”
		ibPlayOrPause.setImageResource(R.drawable.selector_button_play);
		// 4.停止更新进度的线程
		stopUpdateProgressThread();

	}

	// 播放指定位置的歌曲
	private void play(int position) {
		// 判断点击的是否是当前正在播放的
		if (isAppFirstRunning || position != currentMusicIndex) {
			// 计算上一首歌曲的索引
			currentMusicIndex = position;
			// 清空记录的暂停的位置
			pausePosition = 0;
			// 播放
			play();
		}
	}

	// 播放歌曲
	private void play() {
		try {
			// 1. 重置
			player.reset();
			// 2. 设置需要播放的歌曲
			player.setDataSource(musics.get(currentMusicIndex).getPath());
			// 3. 缓冲
			player.prepare();
			// 4. 快进
			player.seekTo(pausePosition);
			// 5. 播放
			player.start();
			// 6. 将按钮的图片设置为“暂停”
			ibPlayOrPause.setImageResource(R.drawable.selector_button_pause);
			// 7. 将当前歌曲标识为正在播放
			for (int i = 0; i < musics.size(); i++) {
				musics.get(i).setPlaying(false);
			}
			musics.get(currentMusicIndex).setPlaying(true);
			adapter.notifyDataSetChanged();// 通过一个外部的方法控
			// 制如果适配器的内容改变时需要强制调用getview来
			// 刷新每个item的内容，可以实现动态的刷新列表的功能。
			// 8. 滑动ListView 控制滑动速度
			lvMusics.smoothScrollToPosition(currentMusicIndex);
			// 9标识为不是刚刚打开应用程序
			isAppFirstRunning = false;
			// 显示当前歌曲的时长
			int duration = player.getDuration();
			txt2.setText(getFormattedTime(duration));
			// 11创建并开启更新进度的线程
			startUpdateProgressThread();
			// 12标识为播放器已经开始工作，用于解决没有播放任何歌曲时拖曳进度条出现的问题
			isPlayWorking = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取歌曲的数据
	private void loadData() {
		// 创建歌曲列表的List集合
		musics = new ArrayList<Music>();
		// 检查SDCARD是否可用
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 确定存放歌曲文件的文件夹(sdcard下的Music文件)
			File musicDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			// 检查Music文件夹是否存在
			if (musicDir.exists()) {
				// 获取该文件夹下的所有File对象
				File[] files = musicDir.listFiles();
				// 检查File数组是否有效
				if (files != null && files.length > 0) {
					// 遍历File数组
					for (int i = 0; i < files.length; i++) {
						// 判断是否是文件
						if (files[i].isFile()) {
							// 判断是否是.mp3文件 （touppercase()转换为大写）以MP3结尾的
							String fileName = files[i].getName();
							if (fileName.toUpperCase(Locale.CHINA).endsWith(
									".MP3")) {
								// 创建Music对象 设置绝对路径
								Music music = new Music();
								music.setTitle(fileName.substring(0,
										fileName.length() - 4));
								music.setPath(files[i].getAbsolutePath());
								// 日志
								Log.v("tedu", "扫描到歌曲：" + music);
								// 将Music对象添加到集合中
								musics.add(music);
							}
						}
					}
				}
			}
		}
	}
}
