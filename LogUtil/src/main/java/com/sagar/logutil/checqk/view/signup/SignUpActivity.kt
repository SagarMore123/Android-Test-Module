package com.astrika.checqk.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.master_controller.sync.MasterSyncIntentService
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.utils.location.LocationActivity
import com.astrika.checqk.view.signup.viewmodels.SignUpViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.PageIndicatorAdapter
import com.sagar.logutil.databinding.ActivitySignUpBinding

class SignUpActivity : LocationActivity(), SignUp, PageIndicatorAdapter.OnItemClickListener {

    lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var pageIndicatorAdapter: PageIndicatorAdapter

    private var callback: SignUp? = null
    private var locationActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startImmediateSync(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        signUpViewModel = Utils.obtainBaseObservable(
            this,
            SignUpViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = signUpViewModel
        binding.lifecycleOwner = this

        setChildActivity(locationActivity)

        pageIndicatorAdapter = PageIndicatorAdapter(this, this)
        binding.pageIndicatorRecyclerView.adapter = pageIndicatorAdapter

        signUpViewModel.pageIndicatorListMutableLiveData.observe(this, Observer {
            pageIndicatorAdapter.arrayList = it
        })

    }


    private fun startImmediateSync(context: Context) {
        val intentToSyncImmediately = Intent(context, MasterSyncIntentService::class.java)
        context.startService(intentToSyncImmediately)
    }


    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is SignUp) {
            callback = fragment
        }
    }

    override fun timingProceed(string: String) {

    }

    override fun fragmentNo(int: Int) {
        when (int) {

            1 -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
            }

            2 -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
            }

            3 -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
            }

            4 -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
            }

            5 -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
            }

            6 -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
            }
            else -> {
                binding.progress1Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_select))
                binding.progress2Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress3Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress4Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress5Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
                binding.progress6Img.setImageDrawable(resources.getDrawable(R.drawable.ic_signup_progress_unselect))
            }
        }


    }

    override fun onBackPressed() {
        finish()
    }

    override fun onItemClick(position: Int) {


    }

}