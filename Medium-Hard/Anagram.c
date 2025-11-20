// Given two strings s1 and s2 consisting of lowercase characters, the task is to check whether the two given
// strings are anagrams of each other or not. An anagram of a string is another string that contains the same
// characters, only the order of characters can be different.
// Input: s1 = “geeks” s2 = “kseeg”
// Output: true
// Explanation: Both the strings have the same characters with same frequency. So, they are anagrams

#include<stdio.h>

void main(){
    char str1[] = "geeks";
    char str2[] = "kseeg";

    int count[26] = {0};

    for(int i = 0; str1[i] != '\0'; i++){
        count[str1[i] - 'a']++;
    }

    for(int i = 0; str2[i] != '\0'; i++){
        count[str2[i] - 'a']--;
    }

    int flag = 1;
    for(int i = 0; i < 26; i++){
        if(count[i] != 0){
            flag = 0;
            break;
        }
    }
    if(flag){
        printf("The strings are anagrams.\n");
    } else {
        printf("The strings are not anagrams.\n");
    }

}