package com.example.shutaffim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shutaffim.Model.Filter
import com.example.shutaffim.ViewModel.PostsVM

@Composable
fun FilterAndSearch(
    postsVM: PostsVM = viewModel()
) {
    var city by remember {
        mutableStateOf("")
    }
    var street by remember {
        mutableStateOf("")
    }
    var priceStart by remember {
        mutableStateOf(0f)
    }
    var priceEnd by remember {
        mutableStateOf(10000f)
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Location")
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = city,
            onValueChange = {
                city = it
                val newFilter = Filter(
                    city = city,
                    street = street,
                    minPrice = priceStart.toInt(),
                    maxPrice = priceEnd.toInt()
                )
                postsVM.filter.value = newFilter
            },
            label = { Text(text = "City") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "City")
            }
        )
        if (city.isNotEmpty()) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = street,
                onValueChange = {
                    street = it
                    val newFilter = Filter(
                        city = city,
                        street = street,
                        minPrice = priceStart.toInt(),
                        maxPrice = priceEnd.toInt()
                    )
                    postsVM.filter.value = newFilter
                },
                label = { Text(text = "Street") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Address")
                }
            )
        }
        Text(text = "Price")
        RangeSlider(
            value = priceStart..priceEnd,
            onValueChange = { range ->
                priceStart = range.start
                priceEnd = range.endInclusive
                val newFilter = Filter(
                    city = city,
                    street = street,
                    minPrice = priceStart.toInt(),
                    maxPrice = priceEnd.toInt()
                )
                postsVM.filter.value = newFilter
            },
            valueRange = 0f..10000f,
            steps = 10000,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "From: ",
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = priceStart.toInt().toString(),
                    onValueChange = {
                        if (it.isNotEmpty() && it.toInt() > 0 && it.toInt() < 9999) {
                            priceStart = it.toFloat()
                        } else if (it.isEmpty()) {
                            priceStart = 0f
                        }
                        val newFilter = Filter(
                            city = city,
                            street = street,
                            minPrice = priceStart.toInt(),
                            maxPrice = priceEnd.toInt()
                        )
                        postsVM.filter.value = newFilter
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "To: ",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                )
                OutlinedTextField(
                    value = priceEnd.toInt().toString(),
                    onValueChange = {
                        if (it.isNotEmpty() && it.toInt() > 0 && it.toInt() < 9999) {
                            priceEnd = it.toFloat()
                        } else if (it.isEmpty()) {
                            priceEnd = 0f
                        }
                        val newFilter = Filter(
                            city = city,
                            street = street,
                            minPrice = priceStart.toInt(),
                            maxPrice = priceEnd.toInt()
                        )
                        postsVM.filter.value = newFilter
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(512.dp))


        }


    }

}

@Preview(showBackground = true)
@Composable
fun fastPreview() {
    FilterAndSearch()
}