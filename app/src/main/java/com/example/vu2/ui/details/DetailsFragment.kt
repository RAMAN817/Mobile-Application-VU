package com.example.vu2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vu2.R
import com.example.vu2.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments ?: return
        val keys = args.getStringArrayList("__keys__") ?: return

        // Rebuild the entity map in original order
        val entity = keys.associateWith { args.getString(it, "") }

        // Title: first non-description field value
        val titleEntry = entity.entries.firstOrNull { it.key != "description" }
        binding.tvDetailTitle.text = titleEntry?.value ?: getString(R.string.details_title)

        // Body fields (exclude description)
        val fields = entity.filter { it.key != "description" }
        binding.tvDetailFields.text = fields.entries.joinToString("\n") { (k, v) ->
            "${formatKey(k)}: $v"
        }

        // Description
        binding.tvDetailDescription.text = entity["description"] ?: "No description available."
    }

    private fun formatKey(key: String): String =
        key.replaceFirstChar { it.uppercase() }.replace("_", " ")

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
