package jp.ac.st.asojuku.original2014002;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MaintenanceActivity extends Activity implements View.OnClickListener {

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
		Button btnback = (Button)findViewById(R.id.btnback);
		btnback.setOnClickListener(this);
		Button btndelete = (Button)findViewById(R.id.btndelete);
		btndelete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		Intent intent = null;
		switch(v.getId()){
			case R.id.btnback:

				intent = new Intent(MaintenanceActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			case R.id.btndelete:

				break;
		}
	}


}
