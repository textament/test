package com.sststore.client;

import android.content.Context;
import android.os.AsyncTask;

import com.sststore.client.utils.Log;
import com.sststore.client.utils.StringUtils;
import com.sststore.client.widgets.CustomDialog;

/**
 * @author Action Tan
 * @date Mar 28, 2012
 */
public abstract class BaseAsyncTask extends
		AsyncTask<AsyncTaskPayload, Object, AsyncTaskPayload> {
	private Context activity;

	protected CustomDialog progressDialog;
	protected String isShow = null;

	public BaseAsyncTask(Context activity, String isShow) {
		this.activity = activity;
		this.isShow = isShow;
	}

	@Override
	protected void onPreExecute() {
		try {
			if (progressDialog == null) {

				progressDialog = new CustomDialog(activity,
						android.R.style.Theme_Translucent_NoTitleBar);

			}
			if (StringUtils.isEmpty(isShow)) {
				progressDialog.show();
			}

			runOnPreExecution();
		} catch (Exception e) {
			Log.d(BaseAsyncTask.class.getSimpleName() + ".onPreExecute",
					"...unknown exception...", e);
		}
	}

	@Override
	protected AsyncTaskPayload doInBackground(AsyncTaskPayload... arg0) {
		try {
			AsyncTaskPayload pld = null;
			if (arg0 != null && arg0.length > 0)
				pld = arg0[0];
			return runOnDoInExecution(pld);
		} catch (Exception e) {
			Log.d(BaseAsyncTask.class.getSimpleName() + ".doInBackground",
					"...unknown exception...", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(AsyncTaskPayload result) {
		super.onPostExecute(result);
		try {
			// check if error happened and the client wants this base async task
			// to handle it or bubble the error to it
			if (result != null && result.getResponse() instanceof ErrorResponse) {

				Log.d(BaseAsyncTask.class.getSimpleName() + ".onPostExecute",
						"...handling error here...");
				if (StringUtils.isEmpty(result.getResponse().getVerification())) {
					((BaseActivity) activity).handleError(
							(ErrorResponse) result.getResponse(), result
									.getResponse().getResult());
				} else {

				}

				runErrorExecution(result);
				return;
			}
			runOnPostExecution(result);
		} catch (Exception e) {
			Log.d(BaseAsyncTask.class.getSimpleName() + ".onPostExecute",
					"...unknown exception...");
		} finally {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
		}
	}

	protected abstract void runOnPreExecution();

	protected abstract AsyncTaskPayload runOnDoInExecution(
			AsyncTaskPayload payload);

	protected abstract void runOnPostExecution(AsyncTaskPayload payload);

	protected abstract void runErrorExecution(AsyncTaskPayload payload);
}
