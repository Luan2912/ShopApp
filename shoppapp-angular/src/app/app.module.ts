import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { OrderComponent } from './order/order.component';
import { OrderConfirmComponent } from './order-confirm/order-confirm.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

@NgModule({
  declarations: [
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    OrderComponent,
    OrderConfirmComponent,
    LoginComponent,
    RegisterComponent
  ],  // Add a comma here
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [
    // HomeComponent,
    // OrderComponent,
    // OrderConfirmComponent
    // LoginComponent
    RegisterComponent
  ]
})

export class AppModule {

}

