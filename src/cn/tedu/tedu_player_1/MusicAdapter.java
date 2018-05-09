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
	// 歌曲数据的List集合
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
		// 1. 根据参数position确定需要显示的数据
		Music music = musics.get(position);

		// 2. 判断是否存在可用的convertView
		if (convertView == null) {
			// 2.1. 如果没有，则根据XML模板加载得到convertView
			LayoutInflater inflater = LayoutInflater.from(context);
			if (getItemViewType(position) == TYPE_DEFAULT) {
				convertView = inflater.inflate(R.layout.list_item_music, null);
			} else {
				convertView = inflater.inflate(R.layout.list_item_music_selected, null);
			}
		}

		// 3. 从convertView中找到具体显示数据的控件
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_music_item_title);
		TextView tvPath = (TextView) convertView.findViewById(R.id.tv_music_item_path);

		// 4. 将数据显示在各控件中
		tvTitle.setText(music.getTitle());
		tvPath.setText(music.getPath());

		// 5. 返回convertView
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
