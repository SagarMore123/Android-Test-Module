package com.sagar.logutil.checqk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sagar.logutil.R
import com.sagar.logutil.checqk.adapters.GalleryImageAdapter
import com.sagar.logutil.checqk.model.GalleryImageCategory

class MainActivity : AppCompatActivity(){

    private lateinit var galleryImagesAdapter: GalleryImageAdapter
    var galleryImageList = ArrayList<GalleryImageCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*

        galleryImagesAdapter = GalleryImageAdapter(this,this)
        recycler.adapter = galleryImagesAdapter
        galleryImageList.add(GalleryImageCategory(1,"Food",1,true))
        galleryImageList.add(GalleryImageCategory(2,"Ambience",2,true))
        galleryImagesAdapter.setGalleryCategoryList(galleryImageList)
*/


    }


}