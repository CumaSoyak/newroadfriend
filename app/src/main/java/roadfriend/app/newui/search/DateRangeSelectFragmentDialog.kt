/*
 * Copyright (c) 2021.
 *
 * Created by Yigit Can YILMAZ
 */

package roadfriend.app.newui.search

import android.os.Bundle
import androidx.core.view.isVisible
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.customviews.DateRangePickerView
import roadfriend.app.databinding.DateRangeSelectFragmentDialogBinding
import roadfriend.app.ui.base.BaseDialogFragment
import roadfriend.app.ui.home.HomeViewModel

class DateRangeSelectFragmentDialog(
    private var isAddReturn: Boolean? = null,
    private val selectAction: (() -> Unit)? = null
) :
    BaseDialogFragment<DateRangeSelectFragmentDialogBinding, HomeViewModel>() {
    override fun createBinding(): DateRangeSelectFragmentDialogBinding {
        return DateRangeSelectFragmentDialogBinding.inflate(layoutInflater)
    }
    override val viewModel: HomeViewModel by viewModel()
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    lateinit var pickerView: DateRangePickerView

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewReady(bundle: Bundle?) {
        arguments?.let { args ->
            if (args.containsKey("isAddReturn")) {
                isAddReturn = args.getBoolean("isAddReturn")
            }
        }
        pickerView = binding.dateRangePicker
        startDate = FlightSearchFragment.flightSearchData.takeoffDate
        endDate = FlightSearchFragment.flightSearchData.returnDate
        initScreen()
    }

    private fun initScreen() {
        binding.ivClose.setOnClickListener { dismiss() }
        pickerView.isShowWeekdayTitleRow(false)
        startDate?.let { pickerView.setStartDate(it) }
        endDate?.let {
            pickerView.isPickDateRange = true
            binding.flReturnDate.isVisible = true
            pickerView.setEndDate(it)
        } ?: kotlin.run {
            pickerView.isPickDateRange = false
        }
        isAddReturn?.let {
            if (it) {
                binding.flReturnDate.isVisible = true
                pickerView.isPickDateRange = true
            }
        }
        setDateObservers()
        binding.llAddReturn.setOnClickListener {
            binding.flReturnDate.isVisible = true
            pickerView.isPickDateRange = true
        }
        binding.ivCancelReturn.setOnClickListener {
            pickerView.isPickDateRange = false
            binding.flReturnDate.isVisible = false
            startDate?.let { pickerView.pickDate(it) }
        }
    }

    private fun setDateObservers() {
        pickerView.startLiveData.observe(viewLifecycleOwner) {
            startDate = it
            FlightSearchFragment.flightSearchData.takeoffDate = it
            binding.tvTakeoffDate.text = DateHelper().dateFormatterForTravel(it)
            selectAction?.invoke()
        }
        pickerView.endLiveData.observe(viewLifecycleOwner) {
            endDate = it
            FlightSearchFragment.flightSearchData.returnDate = it
            binding.tvReturnDate.text = DateHelper().dateFormatterForTravel(it)
            selectAction?.invoke()
        }
    }



}