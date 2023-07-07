package test.project.eva.presentation.screens.camera

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import test.project.eva.R
import test.project.eva.Utils
import test.project.eva.databinding.FragmentCameraBinding
import test.project.eva.extensions.rotate
import test.project.eva.managers.PhotoFilterManager
import test.project.eva.presentation.adapters.FilterRecyclerViewAdapter
import test.project.eva.presentation.screens.base.BaseFragment

@AndroidEntryPoint
class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate),
    ImageAnalysis.Analyzer {

    private val viewModel: CameraViewModel by viewModels()

    private val filterRecyclerViewAdapter: FilterRecyclerViewAdapter by lazy {
        FilterRecyclerViewAdapter()
    }

    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by lazy {
        ProcessCameraProvider.getInstance(requireContext())
    }

    private val imageAnalysis: ImageAnalysis by lazy {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    private var imageCapture: ImageCapture = ImageCapture.Builder().build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViews()
    }

    override fun setUpClicks() {
        binding.captureImageButtonCameraFragment.setOnClickListener {
            takePhoto()
        }
        binding.changeCameraImageButtonCameraFragment.setOnClickListener {
            changeFrontBackCamera()
        }
        binding.galleryImageButtonCameraFragment.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_galleryFragment)
        }
    }

    override fun setUpObservers() {
        viewModel.cameraSelector.observe(this) {
            startCamera(it)
        }
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val capturedImageBitmap = image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())
        setPhotoFilterOnPreview(capturedImageBitmap)
        image.close()
    }

    private fun setPhotoFilterOnPreview(bitmap: Bitmap) {
        try {
            val colorMatrix = viewModel.selectedPhotoFilter.value?.colorMatrix
            binding.filterPreviewImageViewCameraFragment.setImageBitmap(
                Utils.setPhotoFilter(bitmap, colorMatrix)
            )
        } catch (e: Exception) {
            Log.e(TAG, "setPhotoFilterOnPreview: " + e.message)
        }
    }

    private fun initRecyclerViews() {
        binding.filterListRecyclerViewFragmentCamera.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.filterListRecyclerViewFragmentCamera.adapter = filterRecyclerViewAdapter
        filterRecyclerViewAdapter.setUpData(PhotoFilterManager.photoFilterList) { position ->
            viewModel.setSelectedPhotoFilter(filterRecyclerViewAdapter.getItemByPosition(position))
        }
    }

    private fun changeFrontBackCamera() {
        viewModel.changeFrontBackCamera()
    }

    private fun startCamera(cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA) {
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), this)
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, imageAnalysis)
    }

    private fun takePhoto() {
        animateFlash()
        viewModel.takePhoto(imageCapture)
    }

    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 100)
        }, 200)
    }
}