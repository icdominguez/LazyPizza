<img src="assets/lazy_pizza_cover.png" style="width: 100%; height: auto;"/>

**LazyPizza** is an online pizza delivery app built for the Mobile Dev Campus
by [Phillip Lackner](https://pl-coding.com/campus) as part of the monthly challenge. This app is
made for fun and to improve skills in collaboration by three community
members [@extractive-mana-pulse](https://github.com/extractive-mana-pulse), [@galahseno](https://github.com/galahseno)
and [@icdominguez](https://github.com/icdominguez)

## Project Status

This project is divided in 4 different milestones that are launched every fortnight. Was started
being developed at 1st October. We are currently working on Milestone 3.

### 🚨 Latest Features ###

- **All Products Screen**
  - Displays a new icon to login or logout.
  - Log out only deletes cart from remote database (firebase).
- **Login Screen**
  - Displays a text field to type phone number.
  - If number is correct, continue button is enabled and a telegram code will be sent to the phone
    entered.
  - Otp component displayed to enter the code previously sent.
  - If the code is correct, the user will be logged in. If not, an error message will be displayed.
  - Timer for request a new code after a minute.
- **History Screen**
  - Checks if user is logged in or not and if its logged displays a fake order list

## 🧑🏽‍💻 Technical implementation

- ✅ Jetpack Compose.
- ✅ MVI architecture (multi-modularized).
- ✅ Compose Navigator.
- ✅ Koin dependency injection.
- ✅ Backend developed by our own. Firebase database and images stored at Cloudinary.
- ✅ Material Design 3 components and theming.

## 🎥 Demo ##

https://github.com/user-attachments/assets/17985058-5ab6-4322-8470-cbb9d9e5a368

## 📱 Screenshots ##

<details>
  <summary>All Products</summary>

| Mobile                                                                           | Tablet                                                                            | 
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| <img src="assets/screenshots/all_products/all_products_mobile.png" width="600"/> | <img src="assets/screenshots/all_products/all_products_tablet.png" width="1200"/> |

</details>

<details>
  <summary>Login</summary>

| Mobile                                                             | Tablet                                                              | 
|--------------------------------------------------------------------|---------------------------------------------------------------------|
| <img src="assets/screenshots/login/login_mobile.png" width="600"/> | <img src="assets/screenshots/login/login_tablet.png" width="1200"/> |

</details>

<details>
  <summary>Product Detail</summary>

| Mobile                                                                                | Tablet                                                                                 |
|---------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| <img src="assets/screenshots/product_detail/product_detail_mobile.png" width="600" /> | <img src="assets/screenshots/product_detail/product_detail_tablet.png" width="1200" /> |

</details>

<details>
  <summary>Cart</summary>

| Mobile                                                            | Tablet                                                             |
|-------------------------------------------------------------------|--------------------------------------------------------------------|
| <img src="assets/screenshots/cart/cart_mobile.png" width="600" /> | <img src="assets/screenshots/cart/cart_tablet.png" width="1200" /> |

</details>

<details>
  <summary>History</summary>

| Mobile                                                                  | Tablet                                                                   |
|-------------------------------------------------------------------------|--------------------------------------------------------------------------|
| <img src="assets/screenshots/history/history_mobile.png" width="600" /> | <img src="assets/screenshots/history/history_tablet.png" width="1200" /> |

</details>

## 🛠️ Setup

For the otp feature we used the telegram GATEWAY api. You can find documentation here: https://core.telegram.org/gateway.

To test this project you need to add this two variables to your local.properties:

```
telegram_base_url="https://gatewayapi.telegram.org"
telegram_api_token="your_token"
```

Just change telegram_api_token with your token and enjoy 🙌

## 🪪 License

This project is an open-source and free to use. Feel free to fork and upload your commits.

## Acknowledge

- Firebase implementation.
- Mastering Koin dependency.
- Data store for user preferences.
- Navigation rail
- Backend development.
- Responsive design.
- Ktlint for consistent code style.
- Cooperating between co-workers.
- Phone numbers validation.
- Ktor Client.
- Telegram GATEWAY api integration to send code to the users and verify them.

---