package com.sagar.logutil.checqk.utils

import androidx.recyclerview.widget.GridLayoutManager

class MySpanSizeLookup(
    private val spanPos: Int,
    private val spanCnt1: Int,
    private val spanCnt2: Int
) :
    GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {

        /*return when (position and 0x1) {
            1 -> 2
            else -> 1
        }*/

//       return if (position % spanPos == 0) spanCnt2 else spanCnt1

//         return if (position % 3 == 2) {
//            3
//        } else when (position % 4) {
//            1, 3 -> 1
//            0, 2 -> 2
//            else ->                     //never gonna happen
//                -1
//        }


        return if((position + 1) % 3 == 0) 2 else 1
        /*return if(position % 3 == 0 || position % 3 == 1){
            1
        }else{
            2
        }*/

        /*if (position % 3 == 0) {
//                    Log.e("TAG", "Position: " + position +" position % 3= " + position % 3 + " return 2");
//                    return 2;
//                } else {
//                    Log.e("TAG", "Position: " + position +" position % 3= " + position % 3 + " return 1");
//                    return 1;
//                }
            if (position % 3 == 0 || position % 3 == 1) {
//                Log.e("TAG", "Position: " + position +" position % 3= " + position % 3 + " return 1");
                return 1;
            } else {
//                Log.e("TAG", "Position: " + position +" position % 3= " + position % 3 + " return 2");
                return 2;
            }
//                return (3 - position % 3);

        }*/
    }
}