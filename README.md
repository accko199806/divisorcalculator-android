#divisorcalculator-android
-
![Screenshot](https://github.com/accko199806/divisorcalculator-android/blob/master/Screenshot.png?raw=true)

`divisorcalculator-android` 프로젝트는 akoiapp에서 제작한 안드로이드 전용 약수 계산 애플리케이션입니다. 이 코드를 사용해 제작된 애플리케이션은 [여기](https://play.google.com/store/apps/details?id=net.accko.divisorcalculator)에서 확인하실 수 있습니다. 

###핵심 코드
앱에서는 `searchBar`라는 id를 가진 `EditText` 내에서 `IME_ACTION_SEARCH`함수를 호출하여 계산을 처리합니다.

약수의 계산을 처리하는 코드는 아래와 같습니다.
```java

final ArrayList<Long> divisors = new ArrayList();
String enteredNumber = searchBar.getText().toString();
long num = Long.parseLong(searchBar.getText().toString());
```