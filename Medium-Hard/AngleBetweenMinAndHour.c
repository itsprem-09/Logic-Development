#include<stdio.h>

void main(){
    int hour = 3;
    int minute = 30;

    int hourAngle = (hour % 12) * 30 + (minute * 0.5); // Each hour = 30 degrees, each minute = 0.5 degrees
    int minuteAngle = minute * 6; // Each minute = 6 degrees

    int angle = hourAngle - minuteAngle;
    if (angle < 0)
    {
        angle = -angle;
    }
    if (angle > 180)
    {
        angle = 360 - angle;
    }
    printf("Angle between hour and minute hand: %d degrees", angle);
}