package com.example.myscrapnel.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myscrapnel.room_db.ScrapnelEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MyScrapnelViewModel(private val repository: SaveScrapnelRepository) : ViewModel() {

    private val _saveResult = MutableStateFlow<Result<SaveScrapnelResultModel>>(Result.success(SaveScrapnelResultModel(null, null)))
    val saveResult: StateFlow<Result<SaveScrapnelResultModel>> = _saveResult

    private val _theScrapnelToEdit = MutableStateFlow<ScrapnelEntity?>(null)
    val theScrapnelToEdit: StateFlow<ScrapnelEntity?> = _theScrapnelToEdit

    fun saveScrapnel(scrapnel: ScrapnelEntity) {
        viewModelScope.launch {
            try {
                val isExistsInFiveMinutes = repository.isScrapnelWithinFiveMinutes(scrapnel.timeStamp)
                if (!isExistsInFiveMinutes) {
                    repository.insertScrapnel(scrapnel)
                    _saveResult.value = Result.success(SaveScrapnelResultModel(true, scrapnel.timeStamp))
                } else {
                    val existing = repository.existingSimilarTimeStamp
                    _saveResult.value = Result.failure(Exception("Conflict:$existing"))
                }

            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }
    fun loadTheScrapnelToEdit(timeStamp: Long)
    {
        viewModelScope.launch {
            _theScrapnelToEdit.value = repository.getTheScrapnelToEdit(timeStamp)
        }
    }

    fun updateScrapnel(scrapnel: ScrapnelEntity) {
        viewModelScope.launch {
            try {
                repository.updateScrapnel(scrapnel)
                _saveResult.value = Result.success(SaveScrapnelResultModel(true, scrapnel.timeStamp))
            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            }
        }
    }

}


