# divisorcalculator-android
![Screenshot](https://github.com/accko199806/divisorcalculator-android/blob/master/Screenshot.png?raw=true)

`divisorcalculator-android` 프로젝트는 akoiapp에서 제작한 안드로이드 전용 약수 계산 애플리케이션입니다.
이 코드를 사용해 제작된 애플리케이션은 Google Play에서 확인하실 수 있습니다.

<a href="https://play.google.com/store/apps/details?id=net.accko.divisorcalculator">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_60.png" />
</a>

## 핵심 코드
앱에서는 `searchBar`라는 id를 가진 `EditText` 내에서 `IME_ACTION_SEARCH`함수를 호출하여 계산을 처리합니다.

- 약수의 계산을 처리하는 코드는 다음과 같습니다.
```java
ArrayList<Long> divisors = new ArrayList();
String enteredNumber = searchBar.getText().toString();
long num = Long.parseLong(searchBar.getText().toString());
long i = 2;
divisors.add(Long.valueOf(1));
double sqrt = Math.sqrt((double) num);
    
while (((double) i) < sqrt) {
    if (num % i == 0) {
        divisors.add(Long.valueOf(i));
    }
    i++;
}
    
int length = divisors.size() - 1;
if (i * i == num) {
    divisors.add(Long.valueOf(i));
}
    
while (length > 0) {
    divisors.add(Long.valueOf(num / ((Long) divisors.get(length)).longValue()));
    length--;
}

divisors.add(Long.valueOf(num));
```

- 계산된 결과를 출력하는 코드는 다음과 같습니다.
```java
TextUtils.join(", ", divisors)
```

## License
```
Copyright 2018 akoiapp

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```