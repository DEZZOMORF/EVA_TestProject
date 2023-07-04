package test.project.eva.presentation.screens.camera

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
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

    override fun analyze(image: ImageProxy) {
        setPhotoFilterOnPreview()
        image.close()
    }

    private fun setPhotoFilterOnPreview() {
        try {
            val bitmap = binding.previewCameraFragment.bitmap
            val colorMatrix = viewModel.selectedPhotoFilter.value?.colorMatrix
            bitmap?.let {
                binding.filterPreviewImageViewCameraFragment.setImageBitmap(
                    Utils.setPhotoFilter(
                        it, colorMatrix
                    )
                )
            }
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
        val preview: Preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.previewCameraFragment.surfaceProvider)
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), this)
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis)
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