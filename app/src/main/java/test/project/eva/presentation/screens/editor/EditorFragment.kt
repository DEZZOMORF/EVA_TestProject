package test.project.eva.presentation.screens.editor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import test.project.eva.R
import test.project.eva.Utils
import test.project.eva.databinding.FragmentEditorBinding
import test.project.eva.extensions.showToast
import test.project.eva.managers.PhotoFilterManager
import test.project.eva.presentation.adapters.FilterRecyclerViewAdapter
import test.project.eva.presentation.models.PhotoFilter
import test.project.eva.presentation.screens.base.BaseFragment
import test.project.eva.presentation.utils.UIState
import test.project.eva.provider.gallery.SavePhotoState

@AndroidEntryPoint
class EditorFragment : BaseFragment<FragmentEditorBinding>(FragmentEditorBinding::inflate) {

    private val viewModel: EditorViewModel by viewModels()

    private val filterRecyclerViewAdapter: FilterRecyclerViewAdapter by lazy {
        FilterRecyclerViewAdapter()
    }

    private var selectPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val pictureUri = result.data?.data
                viewModel.rememberSelectedPicture(requireContext(), pictureUri)
            }
        }

    override fun setUpObservers() {
        viewModel.getPictureState.observe(this) { selectedPicture ->
            when(selectedPicture) {
                is UIState.Loading -> {
                    binding.progressBarFragmentEditor.isVisible = true
                }
                is UIState.Success -> {
                    binding.progressBarFragmentEditor.isVisible = false
                    binding.pictureImageViewFragmentEditor.setImageDrawable(selectedPicture.data)
                    binding.saveImageButtonFragmentEditor.isVisible = true
                    binding.shareImageButtonFragmentEditor.isVisible = true
                    initRecyclerViews(selectedPicture.data?.toBitmap())
                }
                is UIState.Error -> {
                    binding.progressBarFragmentEditor.isVisible = false
                    Log.e(TAG, "${selectedPicture.exception.message}")
                }
            }
        }

        viewModel.savePictureState.observe(this) { savePictureState ->
            when (savePictureState) {
                is SavePhotoState.Success -> requireContext().showToast(R.string.saved)
                is SavePhotoState.Error -> requireContext().showToast(
                    savePictureState.error.message ?: resources.getString(R.string.error)
                )
            }
        }

        viewModel.photoFilter.observe(this) { photoFilter ->
            val originalBitmap = viewModel.getSelectedPicture()?.toBitmap()
            originalBitmap?.let { bitmap ->
                val bitmapWithFilter = Utils.setPhotoFilter(bitmap, photoFilter?.colorMatrix)
                binding.pictureImageViewFragmentEditor.setImageBitmap(bitmapWithFilter)
            }
        }
    }

    override fun setUpClicks() {
        binding.saveImageButtonFragmentEditor.setOnClickListener {
            val bitmap = binding.pictureImageViewFragmentEditor.drawable.toBitmap()
            savePicture(bitmap)
        }

        binding.shareImageButtonFragmentEditor.setOnClickListener {
            val bitmap = binding.pictureImageViewFragmentEditor.drawable.toBitmap()
            saveAndSharePicture(bitmap)
        }

        binding.randomImageButtonFragmentEditor.setOnClickListener {
            getRandomPhoto()
        }

        binding.selectFromGalleryButtonFragmentEditor.setOnClickListener {
            selectPictureFromGallery()
        }
    }

    private fun initRecyclerViews(image: Bitmap?) {
        image?.let {
            binding.filterListRecyclerViewFragmentEditor.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
            binding.filterListRecyclerViewFragmentEditor.adapter = filterRecyclerViewAdapter
            filterRecyclerViewAdapter.setUpData(PhotoFilterManager.photoFilterList(it)) { position ->
                setPhotoFilter(filterRecyclerViewAdapter.getItemByPosition(position))
            }
        }
    }

    private fun selectPictureFromGallery() {
        viewModel.selectPictureFromGallery(selectPictureLauncher)
    }

    private fun setPhotoFilter(photoFilter: PhotoFilter) {
        viewModel.setPhotoFilter(photoFilter)
    }

    private fun savePicture(bitmap: Bitmap): SavePhotoState {
        return viewModel.savePicture(bitmap)
    }

    private fun getRandomPhoto() {
        viewModel.getRandomPhoto()
    }

    private fun saveAndSharePicture(bitmap: Bitmap) {
        val result = savePicture(bitmap)
        if (result is SavePhotoState.Success) {
            sharePicture(result.data.imageUri)
        }
    }

    private fun sharePicture(imageUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        requireContext().startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
    }
}