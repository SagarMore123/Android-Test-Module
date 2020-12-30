package com.sagar.logutil.checqk.view.dashboard.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.MenuCategoryAdapter
import com.sagar.logutil.checqk.adapters.MenuImagesAdapter
import com.sagar.logutil.checqk.adapters.MenuSubImageAdapter
import com.sagar.logutil.checqk.model.CatalogueImageCategory
import com.sagar.logutil.checqk.model.ImageDTO
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomProgressBar
import com.sagar.logutil.checqk.utils.Utils
import com.sagar.logutil.checqk.view.dashboard.viewmodels.MenuImagesViewModel
import com.sagar.logutil.databinding.FragmentMenuImagesBinding

/**
 * A simple [Fragment] subclass.
 */
class MenuImagesFragment : Fragment(), MenuCategoryAdapter.OnItemClickListener,
    MenuImagesAdapter.OnItemClickListener, MenuSubImageAdapter.OnSubImageClickListener {

    private lateinit var binding: FragmentMenuImagesBinding
    private lateinit var viewModel: MenuImagesViewModel
    private var progressBar = CustomProgressBar()
    private lateinit var menuCategoryAdapter: MenuCategoryAdapter
    private lateinit var menuImagesAdapter: MenuImagesAdapter
    private lateinit var menuSubImageAdapter: MenuSubImageAdapter
    var menuImageCategoryList = ArrayList<CatalogueImageCategory>()
    var menuImagesList = ArrayList<CatalogueImageCategory>()
    var catalogueImageCategoryId = 0L
    private var menuCategorySelectedId: Long = 0L
    var duplicateMenuImageCategoryList = ArrayList<CatalogueImageCategory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu_images,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MenuImagesViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        menuCategoryAdapter = MenuCategoryAdapter(requireContext(), this)
        binding.categoryRecyclerView.adapter = menuCategoryAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMenuImages()

        viewModelObservers()
    }

    private fun viewModelObservers() {
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        viewModel.menuImageCategoryList.observe(viewLifecycleOwner, Observer {
            duplicateMenuImageCategoryList.clear()
            menuCategoryAdapter.setMenuCategoryList(it)
            menuImageCategoryList = it
            duplicateMenuImageCategoryList = menuImageCategoryList

        })

        viewModel.menuImagesList.observe(viewLifecycleOwner, Observer {

            menuImagesList = it

            val newlist = ArrayList<CatalogueImageCategory>()
            var addCategory:Boolean = false
            for(category in duplicateMenuImageCategoryList){
                val categoryDocumentDtos = ArrayList<ImageDTO>()
                for(images in menuImagesList){
                    if(images.catalogueImageCategoryId == category.catalogueImageCategoryId){
                        addCategory = true
                        if(images.documentGetDtos != null){
                            if(!images.documentGetDtos.isNullOrEmpty()){
                                category.showChecked.set(true)
                                categoryDocumentDtos.addAll(images.documentGetDtos!!)
                                category.documentGetDtos = categoryDocumentDtos
                            }
                        }
                    }
                }
                if(addCategory){
                    newlist.add(category)
                    addCategory = false
                }
            }
            duplicateMenuImageCategoryList = newlist

            menuImagesAdapter = MenuImagesAdapter(requireContext(), this, this)
            binding.menuCategoryRecyclerView.adapter = menuImagesAdapter
            menuImagesAdapter.setMenuImagesCategoryList(duplicateMenuImageCategoryList)

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Utils.hideKeyboard(requireActivity())
        when (requestCode) {
            Constants.SELECT_FILE -> {
                onSelectResult(data)
            }
            Constants.CAMERA -> {
                if (data != null) {
                    val imageDTO = ImageDTO()
                    if (data.extras?.get("data") != null) {


                        val thumbnail = data.extras?.get("data") as Bitmap
                        val uri = Utils.getImageUri(requireContext(), thumbnail)
                        uri?.let {
                            imageDTO.imageUri = uri
                        }
                        imageDTO.isUri = true
                        menuSubImageAdapter.imageDTOList.add(imageDTO)
                        menuSubImageAdapter.setImageList(menuSubImageAdapter.imageDTOList)

                        //sending the uris to viewmodel for service call
                        if (imageDTO != null) {
                            viewModel.imageUrisListMutable.value =
                                arrayListOf<Uri>(imageDTO.imageUri!!)
                            viewModel.saveMenuImagesByCategory(catalogueImageCategoryId)
                        }
                    }
                }

            }
        }
    }


    private fun onSelectResult(data: Intent?) {
        if (data != null) {
            val mClipData = data.clipData
            if (mClipData != null) {
                val uriList = ArrayList<Uri>()
                for (i in 0 until mClipData.itemCount) {
                    val item = mClipData.getItemAt(i)
                    val uri = item.uri
                    val galleryImageCategory = ImageDTO()
                    galleryImageCategory.isUri = true
                    uri?.let {
                        galleryImageCategory.imageUri = uri
                        uriList.add(uri)
                        menuSubImageAdapter.imageDTOList.add(galleryImageCategory)
//                        viewModel.imageUrisListMutable.value = gallerySubImageAdapter.galleryImageCategoryList!!
                    }
                    viewModel.imageUrisListMutable.value = uriList
                }
            } else {
                val singleImageUri = data.data
                val galleryImageCategory = ImageDTO()
                galleryImageCategory.isUri = true
                singleImageUri?.let {
                    galleryImageCategory.imageUri = singleImageUri
                }
                menuSubImageAdapter.imageDTOList.add(galleryImageCategory)
                viewModel.imageUrisListMutable.value =
                    arrayListOf<Uri>(galleryImageCategory.imageUri!!)
//                viewModel.imageUrisListMutable.value = galleryImageCategory.uriImagesList
            }
            menuSubImageAdapter.setImageList(menuSubImageAdapter.imageDTOList)
            viewModel.saveMenuImagesByCategory(catalogueImageCategoryId)
        }
    }

    //on click of spinner category
    override fun onMenuCategoryItemClick(
        position: Int,
        catalogueImageCategory: CatalogueImageCategory
    ) {

        if(!catalogueImageCategory.showChecked.get()){
            duplicateMenuImageCategoryList.add(catalogueImageCategory)
            setRecyclerView()
            catalogueImageCategory.showChecked.set(true)
        }else{
            for(item in duplicateMenuImageCategoryList){
                if(catalogueImageCategory.catalogueImageCategoryId == item.catalogueImageCategoryId){
                    if(item.documentGetDtos != null){
                        if(!item.documentGetDtos!!.isNullOrEmpty()){
                            viewModel.showDiscardDialog(catalogueImageCategory,duplicateMenuImageCategoryList)
                            break
                        }else{
                            duplicateMenuImageCategoryList.clear()
                            setRecyclerView()
                            catalogueImageCategory.showChecked.set(false)
                        }
                    }else{
                        duplicateMenuImageCategoryList.remove(catalogueImageCategory)
                        setRecyclerView()
                        catalogueImageCategory.showChecked.set(false)
                        break
                    }
                }
            }
        }
        binding.categoryRecyclerView.visibility = View.GONE
    }

    override fun onAddClick(
        position: Int,
        menuSubImageAdapter: MenuSubImageAdapter,
        catalogueImageCategory: CatalogueImageCategory
    ) {
        menuCategorySelectedId = catalogueImageCategory.catalogueImageCategoryId
        catalogueImageCategoryId = catalogueImageCategory.catalogueImageCategoryId
        this.menuSubImageAdapter = menuSubImageAdapter
        context?.let { it1 -> activity?.let { it2 -> Utils.selectImages(it2, it1, this) } }
    }

    override fun onMenuSubImageRemove(position: Int, imageDTO: ImageDTO) {
        var id: Long = 0
        for (item in menuImagesList) {
            if (item.documentGetDtos != null && item.documentGetDtos?.size!! > 0) {
                id = item.id
                for (images in item.documentGetDtos!!) {
                    if (images.id == imageDTO.id) {
                        imageDTO.id?.let { viewModel.removeMenuImageById(it, id) }
//                        images.documentGetDtos?.remove(imageDTO)
//                        menuSubImageAdapter.notifyItemRemoved(position)
                        break
                    }
                }

            }
        }
    }

    private fun setRecyclerView(){
        val menuImagesAdapter = MenuImagesAdapter(requireContext(), this, this)
        binding.menuCategoryRecyclerView.adapter = menuImagesAdapter
        menuImagesAdapter.setMenuImagesCategoryList(duplicateMenuImageCategoryList)
    }

}
