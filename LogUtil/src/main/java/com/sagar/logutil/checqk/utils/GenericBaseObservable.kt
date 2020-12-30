package com.sagar.logutil.checqk.utils

import android.app.Application
import android.content.Context
import android.view.View
import androidx.databinding.BaseObservable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.opposfeapp.utils.SnackbarMessage
import com.example.opposfeapp.utils.SnackbarMessage.SnackBarObserver
import com.example.opposfeapp.utils.SnackbarUtils

open class GenericBaseObservable(application: Application, owner: LifecycleOwner?, view: View?) : BaseObservable() {
    @JvmField
    val dataLoading = MutableLiveData<Boolean>(false)
    private val mSnackbar = SnackbarMessage()
    fun getmSnackbar(): SnackbarMessage {
        return mSnackbar
    }

    private val mContext: Context
    fun getmContext(): Context {
        return mContext
    }

    fun setupSnackbar(owner: LifecycleOwner?, view: View?) {
        getmSnackbar().observe(owner, object : SnackBarObserver {
            override fun onNewMessage(message: String?) {
                /**
                 * Dialog Message
                 */
                /**
                 * Dialog Message
                 */
                /**
                 * Dialog Message
                 */

                /**
                 * Dialog Message
                 */

/*
                DialogCommonHcpMessageBinding dialogCommonHcpMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplication()), R.layout.dialog_common_hcp_message, (ViewGroup) view, true);

                View view = dialogCommonHcpMessageBinding.getRoot();

                AlertDialog.Builder alert = new AlertDialog.Builder(getApplication());
                // this is set the view from XML inside AlertDialog
        alert.setView(view);
                // disallow cancel of AlertDialog on click of back button and outside touch

                AlertDialog dialog = alert.create();

                dialogCommonHcpMessageBinding.hcpResponseMessageTxt.setText(message);
                dialogCommonHcpMessageBinding.okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

//        String imageUrl = NetworkURLS.SERVER_VAL + mDocumentDTO.getPath();

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.color.md_white_1000);
                dialog.show();
*/
                /**
                 * Toast Message
                 */
                /**
                 * Toast Message
                 */
                /**
                 * Toast Message
                 */
                /**
                 * Toast Message
                 */
//                Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                /** Snackbar message*  */
                /** Snackbar message*  */
                /** Snackbar message*  */
                /** Snackbar message*  */
                SnackbarUtils.showSnackbar(view, message)
            }
        })
    }

    init {
        mContext = application
        owner?.let { setupSnackbar(it, view) }
    }
}