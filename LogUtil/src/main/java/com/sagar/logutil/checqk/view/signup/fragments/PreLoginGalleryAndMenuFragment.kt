package com.astrika.checqk.view.signup.fragments

import android.app.Activity
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
import androidx.navigation.fragment.findNavController
import com.astrika.checqk.model.CatalogueImageCategory
import com.astrika.checqk.model.GalleryImageCategory
import com.astrika.checqk.model.ImageDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.GalleryImagesViewModel
import com.astrika.checqk.view.dashboard.viewmodels.MenuImagesViewModel
import com.astrika.checqk.view.signup.SignUp
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.*
import com.sagar.logutil.databinding.FragmentPreLoginGalleryAndMenuBinding
import com.theartofdev.edmodo.cropper.CropImage

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PreLoginGalleryAndMenuFragment : Fragment(), GalleryCategoryAdapter.OnItemClickListener,
    GalleryImageAdapter.OnItemClickListener, GallerySubImageAdapter.OnSubImageClickListener,
    MenuCategoryAdapter.OnItemClickListener,
    MenuImagesAdapter.OnItemClickListener, MenuSubImageAdapter.OnSubImageClickListener {

    private lateinit var binding: FragmentPreLoginGalleryAndMenuBinding
    private lateinit var galleryImagesViewModel: GalleryImagesViewModel
    private lateinit var menuImagesViewModel: MenuImagesViewModel

    // Gallery Adapters
    private lateinit var galleryGalleryCategoryAdapter: GalleryCategoryAdapter
    private lateinit var galleryImagesAdapter: GalleryImageAdapter
    private lateinit var gallerySubImageAdapter: GallerySubImageAdapter


    var galleryImageCategoryList = ArrayList<GalleryImageCategory>()
    var duplicateGalleryImageCategoryList = ArrayList<GalleryImageCategory>()
    var isProfileImageChange = false
    var imageCategoryPosition = 0
    var galleryCategoryName = ""
    var galleryImagesList = ArrayList<GalleryImageCategory>()
    var galleryImageCategoryId = 0L
    private var galleryCategorySelectedId: Long = 0L


    //Menu Section

    // Menu Adapters
    private lateinit var menuCategoryAdapter: MenuCategoryAdapter
    private lateinit var menuImagesAdapter: MenuImagesAdapter
    private lateinit var menuSubImageAdapter: MenuSubImageAdapter
    var menuImageCategoryList = ArrayList<CatalogueImageCategory>()
    var menuImagesList = ArrayList<CatalogueImageCategory>()
    var catalogueImageCategoryId = 0L
    private var menuCategorySelectedId: Long = 0L
    var duplicateMenuImageCategoryList = ArrayList<CatalogueImageCategory>()

    var isGalleryImageFlag = true

    private var progressBar = CustomProgressBar()
    private lateinit var signUp: SignUp

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pre_login_gallery_and_menu,
            container,
            false
        )
        binding.lifecycleOwner = this

        // Gallery Info Section

        // Gallery ViewModel
        galleryImagesViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            GalleryImagesViewModel::class.java,
            this,
            binding.root
        )!!
        binding.galleryImagesViewModel = galleryImagesViewModel

        galleryGalleryCategoryAdapter = GalleryCategoryAdapter(requireContext(), this)
        binding.galleryCategoryRecyclerView.adapter = galleryGalleryCategoryAdapter


        // Menu Info Section

        // Menu ViewModel
        menuImagesViewModel = Utils.obtainBaseObservable(
            requireActivity(),
            MenuImagesViewModel::class.java,
            this,
            binding.root
        )!!
        binding.menuImagesViewModel = menuImagesViewModel

        menuCategoryAdapter = MenuCategoryAdapter(requireContext(), this)
        binding.menuCategoryRecyclerView.adapter = menuCategoryAdapter


        // Common section

        signUp = activity as SignUp
        signUp.fragmentNo(5)

        return binding.root
    }


    // Gallery Info Section
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelObservers()

        // Gallery Info Section
        galleryImagesViewModel.getGalleryImages()

        binding.profileImageEdit.setOnClickListener {
            isProfileImageChange = true
            context?.let { it1 ->
                activity?.let { it2 ->
                    Utils.showChooseProfileDialog(
                        true,
                        it2,
                        it1,
                        this
                    )
                }
            }
        }

        // Menu Info Section
        menuImagesViewModel.getMenuImages()
        menuImagesViewModel.showProgressBar.value = false


    }

    private fun viewModelObservers() {

        galleryImagesViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        galleryImagesViewModel.isPreLoginContinueClickedMutableLiveData.observe(
            requireActivity(),
            Observer {
                if (it) {
                    findNavController().navigate(R.id.action_PreLoginGalleryAndMenuFragment_to_preLoginSafetyMeasuresFragment)
                }
            })

        galleryImagesViewModel.galleryImageCategoryList.observe(viewLifecycleOwner, Observer {
            duplicateGalleryImageCategoryList.clear()
            galleryGalleryCategoryAdapter.setGalleryCategoryList(it)
            galleryImageCategoryList = it
            duplicateGalleryImageCategoryList = galleryImageCategoryList

        })

        galleryImagesViewModel.galleryImagesList.observe(viewLifecycleOwner, Observer {

            galleryImagesList = it

            val newlist = ArrayList<GalleryImageCategory>()
            var addCategory: Boolean = false
            for (category in duplicateGalleryImageCategoryList) {
                val categoryDocumentDtos = ArrayList<ImageDTO>()
                for (images in galleryImagesList) {
                    if (images.galleryImageCategoryId == category.galleryImageCategoryId) {
                        addCategory = true
                        if (images.documentGetDtos != null) {
                            if (!images.documentGetDtos.isNullOrEmpty()) {
                                category.showChecked.set(true)
                                categoryDocumentDtos.addAll(images.documentGetDtos!!)
                                category.documentGetDtos = categoryDocumentDtos
                            }
                        }
                    }
                }
                if (addCategory) {
                    newlist.add(category)
                    addCategory = false
                }
            }
            duplicateGalleryImageCategoryList = newlist

            galleryImagesAdapter = GalleryImageAdapter(requireContext(), this, this)
            binding.galleryImagesRecyclerView.adapter = galleryImagesAdapter
            galleryImagesAdapter.setGalleryCategoryList(duplicateGalleryImageCategoryList)

        })


        // Menu Info Section

        menuImagesViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        menuImagesViewModel.menuImageCategoryList.observe(viewLifecycleOwner, Observer {
            duplicateMenuImageCategoryList.clear()
            menuCategoryAdapter.setMenuCategoryList(it)
            menuImageCategoryList = it
            duplicateMenuImageCategoryList = menuImageCategoryList

        })

        menuImagesViewModel.menuImagesList.observe(viewLifecycleOwner, Observer {

            menuImagesList = it

            val newlist = ArrayList<CatalogueImageCategory>()
            var addCategory: Boolean = false
            for (category in duplicateMenuImageCategoryList) {
                val categoryDocumentDtos = ArrayList<ImageDTO>()
                for (images in menuImagesList) {
                    if (images.catalogueImageCategoryId == category.catalogueImageCategoryId) {
                        addCategory = true
                        if (images.documentGetDtos != null) {
                            if (!images.documentGetDtos.isNullOrEmpty()) {
                                category.showChecked.set(true)
                                categoryDocumentDtos.addAll(images.documentGetDtos!!)
                                category.documentGetDtos = categoryDocumentDtos
                            }
                        }
                    }
                }
                if (addCategory) {
                    newlist.add(category)
                    addCategory = false
                }
            }
            duplicateMenuImageCategoryList = newlist

            menuImagesAdapter = MenuImagesAdapter(requireContext(), this, this)
            binding.menuImagesRecyclerView.adapter = menuImagesAdapter
            menuImagesAdapter.setMenuImagesCategoryList(duplicateMenuImageCategoryList)

        })

    }

    //on click of spinner category
    override fun onGalleryCategoryItemClick(
        position: Int,
        galleryImageCategory: GalleryImageCategory
    ) {

        isGalleryImageFlag = true

        if (!galleryImageCategory.showChecked.get()) {
            duplicateGalleryImageCategoryList.add(galleryImageCategory)
            setRecyclerView()
            galleryImageCategory.showChecked.set(true)
        } else {
            for (item in duplicateGalleryImageCategoryList) {
                if (galleryImageCategory.galleryImageCategoryId == item.galleryImageCategoryId) {

                    if (item.documentGetDtos != null) {
                        if (!item.documentGetDtos!!.isNullOrEmpty()) {
                            galleryImagesViewModel.showDiscardDialog(
                                galleryImageCategory,
                                duplicateGalleryImageCategoryList
                            )
                            break
                        } else {
                            duplicateGalleryImageCategoryList.clear()
                            setRecyclerView()
                            galleryImageCategory.showChecked.set(false)
                        }
                    } else {
                        duplicateGalleryImageCategoryList.remove(galleryImageCategory)
                        setRecyclerView()
                        galleryImageCategory.showChecked.set(false)
                        break
                    }
                }
            }
        }
        binding.galleryCategoryRecyclerView.visibility = View.GONE
    }


    override fun onAddClick(
        position: Int,
        gallerySubImageAdapter: GallerySubImageAdapter,
        galleryImageCategory: GalleryImageCategory
    ) {
        isGalleryImageFlag = true

        galleryCategorySelectedId = galleryImageCategory.galleryImageCategoryId
        galleryImageCategoryId = galleryImageCategory.galleryImageCategoryId
        this.gallerySubImageAdapter = gallerySubImageAdapter
        context?.let { it1 -> activity?.let { it2 -> Utils.selectImages(it2, it1, this) } }
    }

    // Gallery Info
    override fun onGallerySubImageRemove(position: Int, imageDTO: ImageDTO) {

        var id: Long = 0
        for (item in galleryImagesList) {
            if (item.documentGetDtos != null && item.documentGetDtos?.size!! > 0) {
                id = item.id
                for (images in item.documentGetDtos!!) {
                    if (images.id == imageDTO.id) {
                        galleryImagesViewModel.removeGalleryImageById(imageDTO.id ?: 0, id)
                        break
                    }
                }

            }
        }

    }

    private fun setRecyclerView() {

        if (isGalleryImageFlag) {
            val galleryImagesAdapter = GalleryImageAdapter(requireContext(), this, this)
            binding.galleryImagesRecyclerView.adapter = galleryImagesAdapter
            galleryImagesAdapter.setGalleryCategoryList(duplicateGalleryImageCategoryList)

        } else {
            val menuImagesAdapter = MenuImagesAdapter(requireContext(), this, this)
            binding.menuImagesRecyclerView.adapter = menuImagesAdapter
            menuImagesAdapter.setMenuImagesCategoryList(duplicateMenuImageCategoryList)
        }
    }


    //  Menu Section

    //on click of spinner category
    override fun onMenuCategoryItemClick(
        position: Int,
        catalogueImageCategory: CatalogueImageCategory
    ) {

        isGalleryImageFlag = false
        if (!catalogueImageCategory.showChecked.get()) {
            duplicateMenuImageCategoryList.add(catalogueImageCategory)
            setRecyclerView()
            catalogueImageCategory.showChecked.set(true)
        } else {
            for (item in duplicateMenuImageCategoryList) {
                if (catalogueImageCategory.catalogueImageCategoryId == item.catalogueImageCategoryId) {
                    if (item.documentGetDtos != null) {
                        if (!item.documentGetDtos!!.isNullOrEmpty()) {
                            menuImagesViewModel.showDiscardDialog(
                                catalogueImageCategory,
                                duplicateMenuImageCategoryList
                            )
                            break
                        } else {
                            duplicateMenuImageCategoryList.clear()
                            setRecyclerView()
                            catalogueImageCategory.showChecked.set(false)
                        }
                    } else {
                        duplicateMenuImageCategoryList.remove(catalogueImageCategory)
                        setRecyclerView()
                        catalogueImageCategory.showChecked.set(false)
                        break
                    }
                }
            }
        }
        binding.menuCategoryRecyclerView.visibility = View.GONE
    }

    override fun onAddClick(
        position: Int,
        menuSubImageAdapter: MenuSubImageAdapter,
        catalogueImageCategory: CatalogueImageCategory
    ) {
        isGalleryImageFlag = false
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
                        imageDTO.id?.let { menuImagesViewModel.removeMenuImageById(it, id) }
//                        images.documentGetDtos?.remove(imageDTO)
//                        menuSubImageAdapter.notifyItemRemoved(position)
                        break
                    }
                }

            }
        }
    }


    // Common section
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Utils.hideKeyboard(requireActivity())
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    if (isProfileImageChange) {
                        binding.profileImage.setImageURI(resultUri)
                        galleryImagesViewModel.saveRestaurantProfileImage(resultUri)
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    galleryImagesViewModel.getmSnackbar().value = error.toString()
                }
            }
            Constants.SELECT_FILE -> {
                onSelectResult(data)
            }
            Constants.CAMERA -> {
                if (data != null) {

                    val imageDTO = ImageDTO()
                    val thumbnail = data.extras?.get("data") as Bitmap
                    val uri = Utils.getImageUri(requireContext(), thumbnail)
                    uri?.let {
                        imageDTO.imageUri = uri
                    }
                    imageDTO.isUri = true

                    if (data.extras?.get("data") != null) {

                        // Gallery

                        if (isGalleryImageFlag) {
                            gallerySubImageAdapter.imageDTOList.add(imageDTO)
                            gallerySubImageAdapter.setImageList(gallerySubImageAdapter.imageDTOList)

                            //sending the uris to viewmodel for service call
                            if (imageDTO != null) {
                                galleryImagesViewModel.imageUrisListMutable.value =
                                    arrayListOf<Uri>(imageDTO.imageUri!!)
                                galleryImagesViewModel.saveGalleryImagesByCategory(
                                    galleryImageCategoryId
                                )
                            }

                        } else {

                            // Menu

                            menuSubImageAdapter.imageDTOList.add(imageDTO)
                            menuSubImageAdapter.setImageList(menuSubImageAdapter.imageDTOList)

                            //sending the uris to viewmodel for service call
                            if (imageDTO != null) {
                                menuImagesViewModel.imageUrisListMutable.value =
                                    arrayListOf<Uri>(imageDTO.imageUri!!)
                                menuImagesViewModel.saveMenuImagesByCategory(catalogueImageCategoryId)
                            }

                        }
                    }
                }

            }
        }

    }

    private fun onSelectResult(data: Intent?) {
        if (isGalleryImageFlag) {
            if (data != null) {
                val mClipData = data.clipData
                if (mClipData != null) {
                    val uriList = ArrayList<Uri>()
                    for (i in 0 until mClipData.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        val imageDTO = ImageDTO()
                        imageDTO.isUri = true
                        uri?.let {
                            imageDTO.imageUri = uri
                            uriList.add(uri)
                            gallerySubImageAdapter.imageDTOList.add(imageDTO)
//                        viewModel.imageUrisListMutable.value = gallerySubImageAdapter.galleryImageCategoryList!!
                        }
                        galleryImagesViewModel.imageUrisListMutable.value = uriList
                    }
                } else {
                    val singleImageUri = data.data
                    val imageDTO = ImageDTO()
                    imageDTO.isUri = true
                    singleImageUri?.let {
                        imageDTO.imageUri = singleImageUri
                    }
                    gallerySubImageAdapter.imageDTOList.add(imageDTO)
                    galleryImagesViewModel.imageUrisListMutable.value =
                        arrayListOf<Uri>(imageDTO.imageUri!!)
//                viewModel.imageUrisListMutable.value = galleryImageCategory.uriImagesList
                }
                gallerySubImageAdapter.setImageList(gallerySubImageAdapter.imageDTOList)
                galleryImagesViewModel.saveGalleryImagesByCategory(galleryImageCategoryId)
            }

        } else {
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
                        menuImagesViewModel.imageUrisListMutable.value = uriList
                    }
                } else {
                    val singleImageUri = data.data
                    val galleryImageCategory = ImageDTO()
                    galleryImageCategory.isUri = true
                    singleImageUri?.let {
                        galleryImageCategory.imageUri = singleImageUri
                    }
                    menuSubImageAdapter.imageDTOList.add(galleryImageCategory)
                    menuImagesViewModel.imageUrisListMutable.value =
                        arrayListOf<Uri>(galleryImageCategory.imageUri!!)
//                viewModel.imageUrisListMutable.value = galleryImageCategory.uriImagesList
                }
                menuSubImageAdapter.setImageList(menuSubImageAdapter.imageDTOList)
                menuImagesViewModel.saveMenuImagesByCategory(catalogueImageCategoryId)
            }
        }
    }


}