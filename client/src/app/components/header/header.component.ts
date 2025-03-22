import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  constructor(private router: Router) {}
  // addPerson() {
  //   console.log('Add person clicked!');
  // }
  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
