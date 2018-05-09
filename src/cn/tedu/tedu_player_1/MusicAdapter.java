package cn.tedu.tedu_player_1;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {
	// Context
	private Context context;
	// �������ݵ�List����
	private List<Music> musics;

	public MusicAdapter(Context context, List<Music> musics) {
		super();
		this.context = context;
		this.musics = musics;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	private static final int TYPE_PLAYING = 0;
	private static final int TYPE_DEFAULT = 1;

	@Override
	public int getItemViewType(int position) {
		return musics.get(position).isPlaying() ? TYPE_PLAYING : TYPE_DEFAULT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 1. ���ݲ���positionȷ����Ҫ��ʾ������
		Music music = musics.get(position);

		// 2. �ж��Ƿ���ڿ��õ�convertView
		if (convertView == null) {
			// 2.1. ���û�У������XMLģ����صõ�convertView
			LayoutInflater inflater = LayoutInflater.from(context);
			if (getItemViewType(position) == TYPE_DEFAULT) {
				convertView = inflater.inflate(R.layout.list_item_music, null);
			} else {
				convertView = inflater.inflate(R.layout.list_item_music_selected, null);
			}
		}

		// 3. ��convertView���ҵ�������ʾ���ݵĿؼ�
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_music_item_title);
		TextView tvPath = (TextView) convertView.findViewById(R.id.tv_music_item_path);

		// 4. ��������ʾ�ڸ��ؼ���
		tvTitle.setText(music.getTitle());
		tvPath.setText(music.getPath());

		// 5. ����convertView
		return convertView;
	}

	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
