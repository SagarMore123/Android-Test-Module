package com.astrika.checqk.view.dashboard.fragments

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
import com.astrika.checqk.model.GalleryImageCategory
import com.astrika.checqk.model.ImageDTO
import com.astrika.checqk.utils.Constants
import com.astrika.checqk.utils.CustomProgressBar
import com.astrika.checqk.utils.Utils
import com.astrika.checqk.view.dashboard.viewmodels.GalleryImagesViewModel
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.GalleryCategoryAdapter
import com.sagar.logutil.checqk.adapters.GalleryImageAdapter
import com.sagar.logutil.checqk.adapters.GallerySubImageAdapter
import com.sagar.logutil.databinding.FragmentGalleryImagesBinding
import com.theartofdev.edmodo.cropper.CropImage

/**
 * A simple [Fragment] subclass.
 */
class GalleryImagesFragment : Fragment(), GalleryCategoryAdapter.OnItemClickListener,
    GalleryImageAdapter.OnItemClickListener, GallerySubImageAdapter.OnSubImageClickListener {

    private lateinit var binding: FragmentGalleryImagesBinding
    private lateinit var viewModel: GalleryImagesViewModel
    private var progressBar = CustomProgressBar()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_gallery_images,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            GalleryImagesViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel

        galleryGalleryCategoryAdapter = GalleryCategoryAdapter(requireContext(), this)
        binding.categoryRecyclerView.adapter = galleryGalleryCategoryAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGalleryImages()

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

        viewModelObservers()

    }

    private fun viewModelObservers() {
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()

        })

        viewModel.galleryImageCategoryList.observe(viewLifecycleOwner, Observer {
            duplicateGalleryImageCategoryList.clear()
            galleryGalleryCategoryAdapter.setGalleryCategoryList(it)
            galleryImageCategoryList = it
            duplicateGalleryImageCategoryList = galleryImageCategoryList

        })

        viewModel.galleryImagesList.observe(viewLifecycleOwner, Observer {

            galleryImagesList = it

            val newlist = ArrayList<GalleryImageCategory>()
            var addCategory: Boolean = false
            for (category in duplicateGalleryImageCategoryList) {
                val categoryDocumentDtos = ArrayList<ImageDTO>()
                for (images in galleryImagesList) {
                    if (images.galleryImageCategoryId == category.galleryImageCategoryId) {
                        addCategory = true

                        if (!images.documentGetDtos.isNullOrEmpty()) {
                            category.showChecked.set(true)
                            categoryDocumentDtos.addAll(images.documentGetDtos!!)
                            category.documentGetDtos = categoryDocumentDtos
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
            binding.galleryCategoryRecyclerView.adapter = galleryImagesAdapter
            galleryImagesAdapter.setGalleryCategoryList(duplicateGalleryImageCategoryList)

        })

    }

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
                        viewModel.saveRestaurantProfileImage(resultUri)
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    viewModel.getmSnackbar().value = error.toString()
                }
            }
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
                        gallerySubImageAdapter.imageDTOList.add(imageDTO)
                        gallerySubImageAdapter.setImageList(gallerySubImageAdapter.imageDTOList)

                        //sending the uris to viewmodel for service call
                        viewModel.imageUrisListMutable.value = arrayListOf<Uri>(imageDTO.imageUri!!)
                        viewModel.saveGalleryImagesByCategory(galleryImageCategoryId)
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
                    val imageDTO = ImageDTO()
                    imageDTO.isUri = true
                    uri?.let {
                        imageDTO.imageUri = uri
                        uriList.add(uri)
                        gallerySubImageAdapter.imageDTOList.add(imageDTO)
//                        viewModel.imageUrisListMutable.value = gallerySubImageAdapter.galleryImageCategoryList!!
                    }
                    viewModel.imageUrisListMutable.value = uriList
                }
            } else {
                val singleImageUri = data.data
                val imageDTO = ImageDTO()
                imageDTO.isUri = true
                singleImageUri?.let {
                    imageDTO.imageUri = singleImageUri
                }
                gallerySubImageAdapter.imageDTOList.add(imageDTO)
                viewModel.imageUrisListMutable.value =
                    arrayListOf<Uri>(imageDTO.imageUri!!)
//                viewModel.imageUrisListMutable.value = galleryImageCategory.uriImagesList
            }
            gallerySubImageAdapter.setImageList(gallerySubImageAdapter.imageDTOList)
            viewModel.saveGalleryImagesByCategory(galleryImageCategoryId)
        }
    }

    //on click of spinner category
    override fun onGalleryCategoryItemClick(
        position: Int,
        galleryImageCategory: GalleryImageCategory
    ) {

        if (!galleryImageCategory.showChecked.get()) {
            duplicateGalleryImageCategoryList.add(galleryImageCategory)
            setRecyclerView()
            galleryImageCategory.showChecked.set(true)
        } else {
            for (item in duplicateGalleryImageCategoryList) {
                if (galleryImageCategory.galleryImageCategoryId == item.galleryImageCategoryId) {

                    if (!item.documentGetDtos.isNullOrEmpty()) {
                        viewModel.showDiscardDialog(
                            galleryImageCategory,
                            duplicateGalleryImageCategoryList
                        )
                    } else {
                        duplicateGalleryImageCategoryList.remove(galleryImageCategory)
                        setRecyclerView()
                        galleryImageCategory.showChecked.set(false)
                    }

                    break

                }
            }
        }
        binding.categoryRecyclerView.visibility = View.GONE
    }


    override fun onAddClick(
        position: Int,
        gallerySubImageAdapter: GallerySubImageAdapter,
        galleryImageCategory: GalleryImageCategory
    ) {
        galleryCategorySelectedId = galleryImageCategory.galleryImageCategoryId
        galleryImageCategoryId = galleryImageCategory.galleryImageCategoryId
        this.gallerySubImageAdapter = gallerySubImageAdapter
        context?.let { it1 -> activity?.let { it2 -> Utils.selectImages(it2, it1, this) } }
    }

    override fun onGallerySubImageRemove(position: Int, imageDTO: ImageDTO) {
        var id: Long = 0
        for (item in galleryImagesList) {
            if (!item.documentGetDtos.isNullOrEmpty()) {
                id = item.id
                for (images in item.documentGetDtos ?: arrayListOf()) {
                    if (images.id == imageDTO.id) {
                        viewModel.removeGalleryImageById(imageDTO.id ?: 0, id)
                        break
                    }
                }

            }
        }
    }

    private fun setRecyclerView() {
        val galleryImagesAdapter = GalleryImageAdapter(requireContext(), this, this)
        binding.galleryCategoryRecyclerView.adapter = galleryImagesAdapter
        galleryImagesAdapter.setGalleryCategoryList(duplicateGalleryImageCategoryList)
    }

}
