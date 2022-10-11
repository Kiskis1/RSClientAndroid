package com.example.alt1copy

import android.app.Activity
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.alt1copy.databinding.DetailFragmentBinding


class DetailFragment : Fragment() {
    companion object {
        private const val ARG_MESSAGE = "message"

        fun newInstance(message: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
    }

    private val REQUEST_CODE = 100


    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val message = arguments?.getString(ARG_MESSAGE)


        binding.button.setOnClickListener {
            startProjection()
        }

        binding.button3.setOnClickListener {
            stopProjection()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                requireActivity().startService(ScreenCaptureService.getStartIntent(requireContext(), resultCode, data))
            }
        }
    }

    private fun startProjection() {
        val mProjectionManager =
            requireActivity().getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
        startActivityForResult(mProjectionManager!!.createScreenCaptureIntent(), REQUEST_CODE)
    }
    private fun stopProjection() {
        requireActivity().startService(ScreenCaptureService.getStopIntent(requireContext()));
    }

}
