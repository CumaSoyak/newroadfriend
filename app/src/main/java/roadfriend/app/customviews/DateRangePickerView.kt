/*
 * Copyright (c) 2021.
 *
 * Created by Yigit Can YILMAZ
 */

package roadfriend.app.customviews

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.biletdukkani.b2c.R
import com.biletdukkani.b2c.databinding.CustomDateRangeSelectorBinding
import com.biletdukkani.b2c.databinding.ItemCalendarDayBinding
import com.biletdukkani.b2c.databinding.ItemCalendarRowBinding
import com.biletdukkani.b2c.helpers.GenericAdapter
import roadfriend.app.R
import roadfriend.app.databinding.CustomDateRangeSelectorBinding
import roadfriend.app.databinding.ItemCalendarDayBinding
import roadfriend.app.databinding.ItemCalendarRowBinding
import roadfriend.app.utils.helper.GenericAdapterOld
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import java.util.*

class DateRangePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {
    val binding: CustomDateRangeSelectorBinding = CustomDateRangeSelectorBinding.inflate(LayoutInflater.from(context), this, true)
    var months = listOf("Ocak", "Şubat", "Mart","Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık")
    var days = listOf("PZT", "SAL", "ÇAR", "PER", "CUM", "CMT", "PAZ")
    val startLiveData = MutableLiveData<Calendar?>()
    val endLiveData = MutableLiveData<Calendar?>()
    var isPickDateRange = true
    var monthListAdapter = createMonthListAdapter()
    private val weekDaysTitleLiveData = MutableLiveData(true)
    private @ColorRes var mainSelectionColor: Int? = null
    private @ColorRes var secondarySelectionColor: Int? = null
    private @ColorRes var selectionTextColor: Int? = null
    private @ColorRes var defaultTextColor: Int? = null

    init {
        binding.rvCalendar.adapter = monthListAdapter
        setMainSelectionColor(R.color.blue)
        setSecondarySelectionColor(R.color.blue_secondary)
        setSelectionTextColor(R.color.white)
        setDefaultTextColor(R.color.dark_blue)
    }

    fun setMonthCount(count:Int): DateRangePickerView{
        monthListAdapter = createMonthListAdapter(count)
        startLiveData.removeObservers(context as LifecycleOwner)
        binding.rvCalendar.adapter = monthListAdapter
        monthListAdapter.notifyDataSetChanged()
        return this
    }

    fun isShowWeekdayTitleRow(b: Boolean): DateRangePickerView{
        weekDaysTitleLiveData.postValue(b)
        return this
    }

    fun setMainSelectionColor(@ColorRes color:Int): DateRangePickerView{
        mainSelectionColor = color
        return this
    }

    fun setSecondarySelectionColor(@ColorRes color:Int): DateRangePickerView{
        secondarySelectionColor = color
        return this
    }

    fun setSelectionTextColor(@ColorRes color:Int): DateRangePickerView{
        selectionTextColor = color
        return this
    }


    fun setDefaultTextColor(@ColorRes color:Int): DateRangePickerView{
        defaultTextColor = color
        return this
    }

    fun setStartDate(date: Date){
        val cal = Calendar.getInstance()
        cal.time = date
        startLiveData.postValue(cal)
    }

    fun setStartDate(cal: Calendar?){
        startLiveData.postValue(cal)
    }

    fun getStartDate():Calendar?{
        return startLiveData.value
    }

    fun setEndDate(date: Date){
        val cal = Calendar.getInstance()
        cal.time = date
        endLiveData.postValue(cal)
    }

    fun setEndDate(cal: Calendar?){
        endLiveData.postValue(cal)
    }

    fun getEndDate():Calendar?{
        return endLiveData.value
    }

    fun pickDate(cal: Calendar){
        if (startLiveData.value == null || !isPickDateRange){
            pickOnlyStartDate(cal)
        } else if (startLiveData.value!= null && cal.time.before(startLiveData.value?.time)){
            pickOnlyStartDate(cal)
        } else if (endLiveData.value!=null && cal.time.after(endLiveData.value?.time)){
            pickOnlyStartDate(cal)
        } else{
            setEndDate(cal)
        }
    }

    private fun pickOnlyStartDate(cal: Calendar){
        setEndDate(null)
        setStartDate(cal)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        monthListAdapter.notifyDataSetChanged()
    }

    private fun createMonthListAdapter(numberOfMonths:Int = 12): GenericAdapterOld<GenericAdapterOld<String>> {
        val arrayList = arrayListOf<GenericAdapterOld<String>>()
        for (i in 0..numberOfMonths){
            val targetDate = calculateTargetDate(i)
            val adapter = createDayGridAdapter(targetDate)
            arrayList.add(adapter)

        }
        return GenericAdapterOld(R.layout.item_calendar_row, arrayList){ view, item, position ->
            val itemBinding = ItemCalendarRowBinding.bind(view)
            itemBinding.rvCalendarMonth.adapter = item
            itemBinding.rvCalendarMonth.layoutManager = GridLayoutManager(context, 7)

            val targetDate = calculateTargetDate(position)
            val month = targetDate.get(Calendar.MONTH)
            itemBinding.tvTitleMonth.text = StringBuilder("${months[month]} ${targetDate.get(Calendar.YEAR)}")
            weekDaysTitleLiveData.observeForever{
                itemBinding.tvDayTitles.isVisible = it
            }
            itemBinding.tvTitleDay1.text = days[0]
            itemBinding.tvTitleDay2.text = days[1]
            itemBinding.tvTitleDay3.text = days[2]
            itemBinding.tvTitleDay4.text = days[3]
            itemBinding.tvTitleDay5.text = days[4]
            itemBinding.tvTitleDay6.text = days[5]
            itemBinding.tvTitleDay7.text = days[6]
        }
    }

    private fun calculateTargetDate(plusMonths: Int): Calendar {
        val today = Calendar.getInstance()
        val targetDate = GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH), 1)
        targetDate.add(Calendar.MONTH, plusMonths)
        return targetDate
    }

    private fun createDayGridAdapter(targetDate:Calendar): GenericAdapterOld<String> {
        val dayOfWeek:Int = findDayOfWeek(targetDate)
        val arrayList = arrayListOf<String>()
        for (i in 0 until dayOfWeek){
            arrayList.add("")
        }
        val numberOfDaysInMonth = targetDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..numberOfDaysInMonth){
            arrayList.add(i.toString())
        }
        return GenericAdapterOld(R.layout.item_calendar_day, arrayList){view, item, position ->
            val itemBinding = ItemCalendarDayBinding.bind(view)
            itemBinding.tvDay.text = item
            itemBinding.tvDay.background.colorFilter = getFilter(getColor(R.color.transparent))
            if (item.isNotEmpty()){
                val cellDate = Calendar.getInstance()
                cellDate.time = targetDate.time
                cellDate.set(Calendar.DAY_OF_MONTH, item.toInt())
                if (isPastDay(cellDate)){
                    itemBinding.tvDay.setTextColor(ContextCompat.getColor(context, R.color.grayscale_40))
                } else{
                    itemBinding.tvDay.setTextColor(getColor(defaultTextColor!!))
                    itemBinding.root.setOnClickListener { pickDate(cellDate) }
                }
                setStartDateObserver(itemBinding, cellDate)
                setEndDateObserver(itemBinding, cellDate)

            }
        }
    }

    private fun setStartDateObserver(itemBinding: ItemCalendarDayBinding, cellDate: Calendar) {
        startLiveData.observeForever{ startDateCal ->
            if (isSelectThisCellAsStart(startDateCal, cellDate)){
                clearCell(itemBinding)
                selectCell(itemBinding)
            } else {
                if (!isPastDay(cellDate)){
                    clearCell(itemBinding)
                }
            }
        }
    }

    private fun setEndDateObserver(itemBinding: ItemCalendarDayBinding, cellDate: Calendar) {
        endLiveData.observeForever{ endDateCal ->
            if (isSelectThisCellAsEnd(endDateCal, cellDate)){
                clearCell(itemBinding)
                selectCell(itemBinding)
                itemBinding.viewBgLeftHalf.background = getDrawable(secondarySelectionColor!!)
            } else if (cellDate.after(endDateCal)){
                clearCell(itemBinding)
            }
            if (isSelectThisCellAsBetween(endDateCal, cellDate)){
                clearCell(itemBinding)
                secondarySelectCell(itemBinding)
            }
            if (endDateCal!=null && areDatesEqual(startLiveData.value!!,cellDate)){
                if (areDatesEqual(startLiveData.value!!, endDateCal)){
                    clearCell(itemBinding)
                    selectCell(itemBinding)
                } else {
                    itemBinding.viewBgRightHalf.background = getDrawable(secondarySelectionColor!!)
                }
            }
        }
    }

    private fun clearCell(itemBinding:ItemCalendarDayBinding){
        itemBinding.tvDay.setTextColor(getColor(defaultTextColor!!))
        itemBinding.root.background = getDrawable(R.color.transparent)
        itemBinding.viewBgLeftHalf.background = getDrawable(R.color.transparent)
        itemBinding.viewBgRightHalf.background = getDrawable(R.color.transparent)
        itemBinding.tvDay.background.colorFilter = getFilter(getColor(R.color.transparent))
    }

    private fun selectCell(itemBinding:ItemCalendarDayBinding){
        itemBinding.tvDay.background.colorFilter = getFilter(getColor(mainSelectionColor!!))
        itemBinding.tvDay.setTextColor(getColor(selectionTextColor!!))
    }

    private fun secondarySelectCell(itemBinding:ItemCalendarDayBinding){
        itemBinding.root.background = getDrawable(secondarySelectionColor!!)
        itemBinding.tvDay.background.colorFilter = getFilter(getColor(secondarySelectionColor!!))
    }

    private fun isSelectThisCellAsStart(startDateCal:Calendar?, cellDate:Calendar): Boolean{
        if (startDateCal != null && areDatesEqual(startDateCal,cellDate)){
            return true
        }
        return false
    }

    private fun isSelectThisCellAsEnd(endDateCal:Calendar?, cellDate: Calendar): Boolean{
        if (endDateCal!=null && areDatesEqual(endDateCal, cellDate)){
            return true
        }
        return false
    }

    private fun isSelectThisCellAsBetween(endDateCal:Calendar?, cellDate: Calendar):Boolean{
        if (endDateCal!=null && cellDate.after(startLiveData.value!!) && cellDate.before(endDateCal)){
            return true
        }
        return false
    }

    private fun getFilter(@ColorInt color:Int): ColorFilter? {
        return BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
    }

    private fun getColor(@ColorRes colorResId:Int): Int {
        return ContextCompat.getColor(context, colorResId)
    }

    private fun getDrawable(@DrawableRes drawableResId:Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableResId)
    }

    private fun isPastDay(cellDate:Calendar):Boolean{
        val today = Calendar.getInstance()
        if (cellDate.before(today) && !areDatesEqual(today, cellDate)){
            return true
        }
        return false
    }

    fun findDayOfWeek(cal: Calendar):Int{
        return when(cal.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }
    }

    private fun areDatesEqual(cal1:Calendar, cal2:Calendar): Boolean{
        val day1 = cal1.get(Calendar.DAY_OF_MONTH)
        val day2 = cal2.get(Calendar.DAY_OF_MONTH)
        val month1 = cal1.get(Calendar.MONTH)
        val month2 = cal2.get(Calendar.MONTH)
        val year1 = cal1.get(Calendar.YEAR)
        val year2 = cal2.get(Calendar.YEAR)
        if (day1 == day2 && month1 == month2 && year1 == year2){
            return true
        }
        return false
    }


}