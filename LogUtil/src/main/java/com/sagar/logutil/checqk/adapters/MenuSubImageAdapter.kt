package com.sagar.logutil.checqk.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.model.ImageDTO
import com.astrika.checqk.network.network_utils.SERVER_IMG_URL
import com.bumptech.glide.Glide
import com.sagar.logutil.databinding.MenuImageSubCellLayoutBinding


class MenuSubImageAdapter(
    private val context: Context,val listener:OnSubImageClickListener
) : RecyclerView.Adapter<MenuSubImageAdapter.ImageViewHolder>() {

    var imageDTOList = ArrayList<ImageDTO>()

    fun setImageList(imageDTOList: ArrayList<ImageDTO>) {
        this.imageDTOList = imageDTOList
        notifyDataSetChanged()

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ImageViewHolder(private val binding: MenuImageSubCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            listener: OnSubImageClickListener,
            position: Int,
            imageDTO: ImageDTO
        ) {

            binding.imageView.clipToOutline = true

            if (imageDTO.isUri == true) {
                binding.imageView.setImageURI(imageDTO.imageUri)

            } else {
                Glide.with(context).load("$SERVER_IMG_URL${imageDTO.path}")
                    .into(binding.imageView)
            }

            binding.removeImage.setOnClickListener {
                //open the dialog for confirmation
                showDialog(context,position,imageDTO)
            }

        }

    }

    private fun showDialog(context: Context, position: Int, imageDTO: ImageDTO) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to remove the image?")
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()
            listener.onMenuSubImageRemove(position, imageDTO)
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            MenuImageSubCellLayoutBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val imageDTO = imageDTOList[position]
        holder.bind(context, listener,position, imageDTO)

    }


    override fun getItemCount(): Int {
        return imageDTOList.size
    }

    interface OnSubImageClickListener {
        fun onMenuSubImageRemove(position: Int, imageDTO: ImageDTO)
    }

}

