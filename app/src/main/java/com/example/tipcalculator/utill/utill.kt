package com.example.tipcalculator.utill

fun calculateTotalTip(totalbill: Double, tipPercentage: Int): Double {
    return if(totalbill >1 && totalbill.toString().isNotEmpty())
        (totalbill * tipPercentage) / 100
    else 0.0

}

fun calculateTotalPerPerson(
    totalbill: Double,
    splitBy: Int,
    tipPercentage: Int): Double{
    val bill = calculateTotalTip(totalbill = totalbill, tipPercentage = tipPercentage) + totalbill

    return (bill /splitBy)
}