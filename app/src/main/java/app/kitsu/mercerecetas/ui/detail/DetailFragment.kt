/*
 *  Copyright 2019, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package app.kitsu.mercerecetas.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import app.kitsu.mercerecetas.MainActivity

import app.kitsu.mercerecetas.databinding.FragmentDetailBinding


/**
 * This [Fragment] will show the detailed information about a selected piece of Mars real estate.
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        //getting value from safeArgs
        val recipe = DetailFragmentArgs.fromBundle(arguments!!).selectedRecipe
        val factory = DetailViewModelFactory(recipe, application)

         viewModel = ViewModelProvider(this,factory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.updateActionBarTitle(recipe.name)

        viewModel.title.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).supportActionBar?.title = it
        })

        return binding.root
    }
}