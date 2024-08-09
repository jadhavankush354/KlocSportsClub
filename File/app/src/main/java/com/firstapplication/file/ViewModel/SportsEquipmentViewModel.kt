package com.firstapplication.file.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.R
import com.firstapplication.file.SportsEquipmentEntity
import com.firstapplication.file.Repository.SportsEquipmetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class SportsEquipmentViewModel @Inject constructor(
    private val repository: SportsEquipmetRepository
) : ViewModel() {
    fun insertSports(sportsEquipmentEntity: SportsEquipmentEntity) {
        viewModelScope.launch(IO) {
            repository.insertSport(sportsEquipmentEntity)
        }
    }
    private val _equipmentSize = MutableStateFlow<String?>(null)
     val equipmentSize: StateFlow<String?>
        get() = _equipmentSize

    private val _equipmentFound = MutableStateFlow(false)
    val equipmentFound: StateFlow<Boolean>
        get() = _equipmentFound

    private val _equipmentImageResId = MutableStateFlow(R.drawable.ic_launcher_background)
    val equipmentImageResId: StateFlow<Int>
        get() = _equipmentImageResId


    fun getEquipmentSizeByAge(equipment: String, equipmentType: String, categories: String, playerAge: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val size = repository.getEquipmentSizeByAge(equipment, equipmentType, categories, playerAge)
                updateEquipmentValues(size)
                withContext(Dispatchers.Main) {
                    _equipmentSize.value = size?.toString() ?: ""
                    _equipmentFound.value = !_equipmentSize.value.isNullOrBlank()
                    _equipmentImageResId.value = when {
                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                && equipmentType.equals("Leather", ignoreCase = true)
                                && categories.equals("KashmirWillow", ignoreCase = true) -> R.drawable.batsizes
                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                && equipmentType.equals("Leather", ignoreCase = true)
                                && categories.equals("EnglishWillow", ignoreCase = true) -> R.drawable.batsizes
                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                && equipmentType.equals("Leather", ignoreCase = true)
                                && categories.equals("LongHandleBats", ignoreCase = true) -> R.drawable.batsizes
                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                && equipmentType.equals("Tennis", ignoreCase = true)
                                && categories.equals("HardTennis", ignoreCase = true) -> R.drawable.batsizes
                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                && equipmentType.equals("Tennis", ignoreCase = true)
                                && categories.equals("lowTennis", ignoreCase = true) -> R.drawable.batsizes
                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Gloves", ignoreCase = true)
                                && categories.equals("HandGloves", ignoreCase = true) -> R.drawable.battingglovesbestimage
                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Pads", ignoreCase = true)
                                && categories.equals("LegPads", ignoreCase = true) -> R.drawable.padandgloveschart
                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Pads", ignoreCase = true)
                                && categories.equals("ThighGuards", ignoreCase = true) -> R.drawable.thighpadsexact
                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Helmet", ignoreCase = true)
                                && categories.equals("Helmet", ignoreCase = true) -> R.drawable.besthelmetsizeguide
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Spiky", ignoreCase = true)
                                && categories.equals("Male", ignoreCase = true) -> R.drawable.ukshoeschart
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Non-Spiky", ignoreCase = true)
                                && categories.equals("Male", ignoreCase = true) -> R.drawable.ukshoeschart
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Spiky", ignoreCase = true)
                                && categories.equals("Female", ignoreCase = true) -> R.drawable.ukshoeschart
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Non-Spiky", ignoreCase = true)
                                && categories.equals("Female", ignoreCase = true) -> R.drawable.ukshoeschart
                        else -> R.drawable.ic_launcher_background
                    }
                }
            } catch (e: Exception) {
                Log.e("EquipmentSize", "Error: ${e.message}", e)
            }
        }
    }

    fun getEquipmentSizeByHeight(
        equipment: String,
        equipmentType: String,
        categories: String,
        playerHeight: Double)
    {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val size = repository.getEquipmentSizeByHeight(
                    equipment,
                    equipmentType,
                    categories,
                    playerHeight
                )
                updateEquipmentValues(size)
                withContext(Dispatchers.Main)
                {
                    _equipmentSize.value = size?.toString() ?: ""
                    _equipmentFound.value = !_equipmentSize.value.isNullOrBlank()
                    _equipmentImageResId.value = when
                    {
                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                &&equipmentType.equals("Leather",ignoreCase = true)
                                &&categories.equals("KashmirWillow",ignoreCase = true)
                        ->R.drawable.batsizes

                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                &&equipmentType.equals("Leather",ignoreCase = true)
                                &&categories.equals("EnglishWillow",ignoreCase = true)
                        ->R.drawable.batsizes

                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                &&equipmentType.equals("Leather",ignoreCase = true)
                                &&categories.equals("LongHandleBats",ignoreCase = true)
                        ->R.drawable.batsizes

                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                &&equipmentType.equals("Tennis",ignoreCase = true)
                                &&categories.equals("HardTennis",ignoreCase = true)
                        ->R.drawable.batsizes

                        _equipmentFound.value && equipment.equals("Bat", ignoreCase = true)
                                &&equipmentType.equals("Tennis",ignoreCase = true)
                                &&categories.equals("lowTennis",ignoreCase = true)
                        ->R.drawable.batsizes

                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Gloves",ignoreCase = true)
                                &&categories.equals("HandGloves",ignoreCase = true)-> R.drawable.battingglovesbestimage

                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Pads",ignoreCase = true)
                                &&categories.equals("LegPads",ignoreCase = true)-> R.drawable.padandgloveschart

                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Pads",ignoreCase = true)
                                &&categories.equals("ThighGuards",ignoreCase = true)-> R.drawable.thighpadsexact
                        _equipmentFound.value && equipment.equals("ProtectiveGear", ignoreCase = true)
                                && equipmentType.equals("Helmet",ignoreCase = true)
                                &&categories.equals("Helmet",ignoreCase = true)-> R.drawable.besthelmetsizeguide

                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Male",ignoreCase = true)
                                &&categories.equals("Spiky",ignoreCase = true)-> R.drawable.ukshoeschart
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Male",ignoreCase = true)
                                &&categories.equals("Non-Spiky",ignoreCase = true)-> R.drawable.ukshoeschart
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Female",ignoreCase = true)
                                &&categories.equals("Spiky",ignoreCase = true)-> R.drawable.ukshoeschart
                        _equipmentFound.value && equipment.equals("Shoes", ignoreCase = true)
                                && equipmentType.equals("Female",ignoreCase = true)
                                &&categories.equals("Non-Spiky",ignoreCase = true)-> R.drawable.ukshoeschart
                        else ->
                            R.drawable.ic_launcher_background
                    }
                }
            } catch (e: Exception) {
                Log.e("EquipmentSize", "Error: ${e.message}", e)
            }
        }
    }
    private suspend fun updateEquipmentValues(size: String?) {
        withContext(Dispatchers.Main) {
            _equipmentSize.value = size?.toString() ?: "N/A"
            _equipmentFound.value = !_equipmentSize.value.isNullOrBlank()
            _equipmentImageResId.value =
                if (_equipmentFound.value) R.drawable.batsizes else R.drawable.ic_launcher_background
        }
    }

    fun resetEquipmentDetails() {
        // Reset all relevant state back to initial conditions
        _equipmentFound.value = false
        _equipmentSize.value = ""
        _equipmentImageResId.value = 0  // Assuming 0 is a placeholder or default value
    }

    private fun determineImageResId(equipment: String, equipmentType: String, categories: String, equipmentFound: Boolean): Int {
        if (!equipmentFound) return R.drawable.ic_launcher_background

        return when {
            equipment.equals("Bat", ignoreCase = true) && equipmentType.equals("Leather", ignoreCase = true) -> {
                when (categories) {
                    "KashmirWillow", "EnglishWillow", "LongHandleBats" -> R.drawable.batsizes
                    else -> R.drawable.ic_launcher_background
                }
            }
            equipment.equals("Bat", ignoreCase = true) && equipmentType.equals("Tennis", ignoreCase = true) -> {
                when (categories) {
                    "HardTennis", "lowTennis" -> R.drawable.batsizes
                    else -> R.drawable.ic_launcher_background
                }
            }
            equipment.equals("ProtectiveGear", ignoreCase = true) -> {
                when {
                    equipmentType.equals("Gloves", ignoreCase = true) && categories.equals("HandGloves", ignoreCase = true) -> R.drawable.battingglovesbestimage
                    equipmentType.equals("Pads", ignoreCase = true) -> {
                        when (categories) {
                            "legPads" -> R.drawable.padandgloveschart
                            "ThighGuards" -> R.drawable.thighpadsexact
                            else -> R.drawable.ic_launcher_background
                        }
                    }
                    equipmentType.equals("Helmet", ignoreCase = true) && categories.equals("Helmet", ignoreCase = true) -> R.drawable.besthelmetsizeguide
                    else -> R.drawable.ic_launcher_background
                }
            }
            else -> R.drawable.ic_launcher_background
        }
    }
}


