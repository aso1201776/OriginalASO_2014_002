package jp.ac.st.asojuku.original2014002;

import android.app.Activity;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MaintenanceActivity extends Activity implements
	 View.OnClickListener, AdapterView.OnItemClickListener {

	//SQLiteデータベース空間を差往査するインスタンス変数を宣言
	SQLiteDatabase sdb = null;
	//MySQLiteOpenHelperを操作するインスタンス変数を宣言
	MySQLiteOpenHelper helper = null;

	//リストにて選択したHitokotoテーブルのレコードの｢_id｣カラム値を保持する変数の宣言
	int selectedID = -1;
	//リストにて選択した行番号を保持する変数の宣言
	int lastPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintenance_activity);
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();

		//各view部品を操作するidを取得
		Button btnback = (Button)findViewById(R.id.btnback);
		Button btndelete = (Button)findViewById(R.id.btndelete);
		ListView lstHitokoto = (ListView)findViewById(R.id.lstHitokoto);

		//各ButtonにOnClickListnerをセット
		btnback.setOnClickListener(this);
		btndelete.setOnClickListener(this);

		//ListViewにOnItemClickListenerをセット
		lstHitokoto.setOnItemClickListener(this);

		//ListViewにDBの値をセット
		this.setDBValuetoList(lstHitokoto);
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		switch(v.getId()){
			case R.id.btnback:
				//今の画面Activityを消して、前の画面Activityに戻る
				finish();
				break;
			case R.id.btndelete:
				//選択行があれば
				if(this.selectedID != -1) {
					this.deleteFromHitokoto(this.selectedID);
					ListView lstHitokoto = (ListView)findViewById(R.id.lstHitokoto);
					//ListViewにDBをセット
					this.setDBValuetoList(lstHitokoto);
					//選択行を忘れる
					this.selectedID = -1;
					this.lastPosition = -1;
				} else {
					//なければ、トースト(簡易メッセージ）を表示
					Toast.makeText(MaintenanceActivity.this, "削除する行を選んでください。", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	/**
	 * @param AdapterView<?> parent クリックしたListView
	 * @param View view クリックしたListViewの中の各行
	 * @param int position 何行目をクリックしたか
	 * @param long viewid 未使用
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 自動生成されたメソッド・スタブ
		//前に選択中の行があれば背景色を透明にする
		if(this.selectedID!=-1){
			parent.getChildAt(this.lastPosition).setBackgroundColor(0);
		}
		//選択中の行の背景色をグレーにする
		view.setBackgroundColor(android.graphics.Color.LTGRAY);

		//選択中のレコードを指し示すカーソルを取得
		SQLiteCursor cursor = (SQLiteCursor)parent.getItemAtPosition(position);
		//カーソルのレコードから｢_id｣の値を取得して記憶
		this.selectedID = cursor.getInt(cursor.getColumnIndex("_id"));
		//何行目を選択したかも記憶
		this.lastPosition = position;
	}

	/*
	 * 引数のListViewにDBのデータをセット
	 * @param lstHitokoto 対象となるListView
	 */
	private void setDBValuetoList(ListView lstHitokoto) {

		SQLiteCursor cursor = null;

		//クラスのフィールド変数がnullなら、データベース空間をオープン
		if(sdb == null) {
			helper = new MySQLiteOpenHelper(getApplicationContext());
		} try {
			sdb = helper.getWritableDatabase();
		} catch (SQLiteException e) {
			//異常終了
			Log.e("ERROR", e.toString());
		}
		//MySQLiteOpenHelperにSELECT文を実行させて結果のカーソルを受け取る
		cursor = this.helper.selectHitokotoList(sdb);

		//dblayout: ListViewにさらにレイアウトを指定するもの
		int db_layout = android.R.layout.simple_list_item_activated_1;
		//from: カーソルからListViewに指定するカラムの値を指定するもの
		String[]from = {"phrase"};
		//to: ListViewの中に指定したdb_layoutに配置する、各行のview部品のid
		int[] to = new int[]{android.R.id.text1};

		//ListView二セットするアダプターを生成
		//カーソルをもとにfrom列からtoのviewへ値のマッピングが行われる。
		SimpleCursorAdapter adapter =
				new SimpleCursorAdapter(this,db_layout,cursor,from,to,0);

		//アダプターを設定
		lstHitokoto.setAdapter(adapter);
	}

	/**
	 * Hitokotoテーブルから、引数で指定した｢_id｣と同じ値を持つレコードを削除
	 * @param id 指定する値
	 */
	private void deleteFromHitokoto(int id) {
		//クラスのフィールド変数がNULLならデータベース空間オープン
		if(sdb == null) {
			helper = new MySQLiteOpenHelper(getApplicationContext());
		} try {
			sdb = helper.getWritableDatabase();
		} catch(SQLiteException e) {
			//異常終了
			Log.e("ERROR", e.toString());
		}
		//MySQLiteOpenHelperにDELETE文を実行させる
		this.helper.deleteHitokoto(sdb, id);
	}
}
