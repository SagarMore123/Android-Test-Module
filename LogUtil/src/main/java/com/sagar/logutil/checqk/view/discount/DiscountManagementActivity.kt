package com.sagar.logutil.checqk.view.discount

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.discount.DiscountCategoriesAdapter
import com.sagar.logutil.checqk.model.discount.DiscountCategoryDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.DashboardActivity
import com.sagar.logutil.checqk.view.discount.viewmodels.DiscountViewModel
import com.sagar.logutil.databinding.ActivityDiscountManagementBinding

class DiscountManagementActivity : AppCompatActivity(),
    DiscountCategoriesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityDiscountManagementBinding
    lateinit var navController: NavController
    private lateinit var discountViewModel: DiscountViewModel
    private lateinit var discountCategoriesAdapter: DiscountCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discount_management)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        discountViewModel = Utils.obtainBaseObservable(
            this,
            DiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.discountViewModel = discountViewModel
        binding.lifecycleOwner = this

        discountCategoriesAdapter = DiscountCategoriesAdapter(this, this)
        binding.discountCategoryRecyclerView.adapter = discountCategoriesAdapter

        observer()


    }

    fun observer() {

        discountViewModel.discountCategoryListMutableLiveData.observe(this, Observer {
            discountCategoriesAdapter.list = it
        })

        discountViewModel.observableBoolean.observe(this, Observer {
            if (it) {
                val bundle = Bundle()
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.membershipDiscountArrayList
                )
                navController.navigate(R.id.MembershipDiscountFragment, bundle)
            }
        })

    }

    override fun onItemClick(position: Int, discountCategoryDTO: DiscountCategoryDTO) {
        discountViewModel.categorySelection(position, discountCategoryDTO)
        displayFragment(position)
    }

    private fun displayFragment(position: Int) {

        val bundle = Bundle()
        when (position) {

            0 -> {

                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.membershipDiscountArrayList
                )
                navController.navigate(R.id.MembershipDiscountFragment, bundle)
            }
            1 -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.cravxCardDiscountArrayList
                )
                navController.navigate(R.id.CravXCardDiscountFragment, bundle)
            }
            2 -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.hwCardDiscountArrayList
                )
                navController.navigate(R.id.HungryWungryDiscountFragment, bundle)
            }
            3 -> {
                bundle.putSerializable(
                    Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY,
                    discountViewModel.oneDashboardDiscountMastersArrayList
                )
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.oneDashboardDiscountArrayList
                )

                navController.navigate(R.id.OneDashboardFragment, bundle)
            }
            4 -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.inHouseDiscountArrayList
                )
                navController.navigate(R.id.InHouseCardsFragment, bundle)
            }

            else -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.membershipDiscountArrayList
                )
                navController.navigate(R.id.MembershipDiscountFragment, bundle)
            }
        }

    }

    override fun onBackPressed() {
        Constants.changeActivity<DashboardActivity>(this)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}