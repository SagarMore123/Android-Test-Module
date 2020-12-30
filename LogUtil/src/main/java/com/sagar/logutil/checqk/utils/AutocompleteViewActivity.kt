package com.sagar.logutil.checqk.utils

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.CommonAutoCompleteListAdapter
import com.sagar.logutil.checqk.adapters.GroupRoleAutocompleteListAdapter
import com.sagar.logutil.checqk.adapters.masters.*
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.databinding.ActivityAutocompleteViewBinding

class AutocompleteViewActivity : AppCompatActivity(),
    CuisineAutocompleteListAdapter.OnItemClickListener,
    CountryAutocompleteListAdapter.OnItemClickListener,
    StateAutocompleteListAdapter.OnItemClickListener,
    CityAutocompleteListAdapter.OnItemClickListener,
    AreaAutocompleteListAdapter.OnItemClickListener,
    SocialMediaMastersAdapter.OnItemClickListener,
    GroupRoleAutocompleteListAdapter.OnItemClickListener,
    CorporatesMembershipOneDashboardAutocompleteListAdapter.OnItemClickListener,
    CommonAutoCompleteListAdapter.OnItemClickListener{

    private lateinit var binding: ActivityAutocompleteViewBinding
    private lateinit var viewModel: AutocompleteViewModel
    private lateinit var cuisineAutocompleteListAdapter: CuisineAutocompleteListAdapter
    private lateinit var countryAutocompleteListAdapter: CountryAutocompleteListAdapter
    private lateinit var stateAutocompleteListAdapter: StateAutocompleteListAdapter
    private lateinit var cityAutocompleteListAdapter: CityAutocompleteListAdapter
    private lateinit var areaAutocompleteListAdapter: AreaAutocompleteListAdapter
    private lateinit var socialMediaMastersAdapter: SocialMediaMastersAdapter
    private lateinit var groupRoleAutoCompleteAdapter: GroupRoleAutocompleteListAdapter
    private lateinit var corporatesMembershipOneDashboardAutocompleteListAdapter: CorporatesMembershipOneDashboardAutocompleteListAdapter
    private lateinit var commonAutoCompleteListAdapter: CommonAutoCompleteListAdapter

    private var resultCode = 0
    var countryId = 0L
    var stateId = 0L
    var cityId = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        initBinding()
        observers()
    }

    private fun initBinding() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_autocomplete_view)
        viewModel = Utils.obtainBaseObservable(
            this,
            AutocompleteViewModel::class.java,
            this,
            binding.root
        )!!
        binding.lifecycleOwner = this
        binding.autocompleteViewModel = viewModel

        cuisineAutocompleteListAdapter = CuisineAutocompleteListAdapter(this)
        binding.cuisineRecyclerView.adapter = cuisineAutocompleteListAdapter

        countryAutocompleteListAdapter = CountryAutocompleteListAdapter(this)
        binding.countryRecyclerView.adapter = countryAutocompleteListAdapter

        stateAutocompleteListAdapter = StateAutocompleteListAdapter(this)
        binding.stateRecyclerView.adapter = stateAutocompleteListAdapter

        cityAutocompleteListAdapter = CityAutocompleteListAdapter(this)
        binding.cityRecyclerView.adapter = cityAutocompleteListAdapter

        areaAutocompleteListAdapter = AreaAutocompleteListAdapter(this)
        binding.areaRecyclerView.adapter = areaAutocompleteListAdapter

        socialMediaMastersAdapter = SocialMediaMastersAdapter(this, this)
        binding.socialMediaIconRecyclerView.adapter = socialMediaMastersAdapter

        groupRoleAutoCompleteAdapter = GroupRoleAutocompleteListAdapter(this)
        binding.groupRolesRecyclerView.adapter = groupRoleAutoCompleteAdapter

        corporatesMembershipOneDashboardAutocompleteListAdapter = CorporatesMembershipOneDashboardAutocompleteListAdapter(this)
        binding.corporatesMembershipOneDashboardRecyclerView.adapter = corporatesMembershipOneDashboardAutocompleteListAdapter

        commonAutoCompleteListAdapter = CommonAutoCompleteListAdapter(this)
        binding.commonRecyclerView.adapter = commonAutoCompleteListAdapter


        resultCode = intent.getIntExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, 0)
        countryId = intent.getLongExtra(Constants.COUNTRY_ID_KEY, 0)
        stateId = intent.getLongExtra(Constants.STATE_ID_KEY, 0)
        cityId = intent.getLongExtra(Constants.CITY_ID_KEY, 0)

        if (intent?.getSerializableExtra(Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY) != null) {
            viewModel.corporateMembershipOneDashboardDiscountMasterArrayList =
                intent?.getSerializableExtra(Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY) as ArrayList<CorporateMembershipOneDashboardDTO>

        }

        viewModel.populateMainList(resultCode, countryId, stateId, cityId)

    }

    private fun observers() {

        viewModel.autocompleteText.observe(this, Observer {
            viewModel.searchByText(resultCode, it)
        })

        viewModel.cuisineListMutableLiveData.observe(this, Observer {
            cuisineAutocompleteListAdapter.list = it
        })

        viewModel.countryMasterListMutableLiveData.observe(this, Observer {
            countryAutocompleteListAdapter.list = it
        })

        viewModel.stateMasterListMutableLiveData.observe(this, Observer {
            stateAutocompleteListAdapter.list = it
        })

        viewModel.cityMasterListMutableLiveData.observe(this, Observer {
            cityAutocompleteListAdapter.list = it
        })


        viewModel.areaMasterListMutableLiveData.observe(this, Observer {
            areaAutocompleteListAdapter.list = it
        })

        viewModel.socialMediaMasterListMutableLiveData.observe(this, Observer {
            socialMediaMastersAdapter.submitList(it)
        })

        viewModel.groupRolesListMutableLiveData.observe(this, Observer {
            groupRoleAutoCompleteAdapter.list = it
        })

        // Corporate Membership One Dashboard Master
        viewModel.corporateMembershipOneDashboardDiscountMasterListMutableLiveData.observe(this, Observer {
            corporatesMembershipOneDashboardAutocompleteListAdapter.list = it
        })

        viewModel.commonMutableList.observe(this, Observer {
            commonAutoCompleteListAdapter.list = it
        })

    }


    override fun onItemClick(position: Int, cuisineMasterDTO: CuisineMasterDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, cuisineMasterDTO)
        setResult(resultCode, resultIntent)
        finish()

    }

    override fun onItemClick(position: Int, countryMasterDTO: CountryMasterDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, countryMasterDTO)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun onItemClick(position: Int, stateMasterDTO: StateMasterDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, stateMasterDTO)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun onItemClick(position: Int, cityMasterDTO: CityMasterDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, cityMasterDTO)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun onItemClick(position: Int, areaMasterDTO: AreaMasterDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, areaMasterDTO)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun onItemClick(position: Int, socialMediaMasterDTO: SocialMediaMasterDTO) {

        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, socialMediaMasterDTO)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun onItemClick(position: Int, groupRolesDTO: GroupRolesDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, groupRolesDTO)
        setResult(resultCode, resultIntent)
        finish()
    }

    // Corporate Membership One Dashboard Master
    override fun onItemClick(position: Int, dto: CorporateMembershipOneDashboardDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, dto)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun onItemClick(position: Int, commonDialogDTO: CommonDialogDTO) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SELECTED_DROPDOWN_ITEM, commonDialogDTO)
        setResult(resultCode, resultIntent)
        finish()
    }


}
