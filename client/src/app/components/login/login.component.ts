import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login(this.email, this.password).subscribe(
      (response) => {
        this.authService.saveToken(response.token); // ✅ Save token in AuthService
        this.router.navigate(['/']); // Redirect to home
      },
      () => {
        this.errorMessage = 'Invalid email or password';
      }
    );
  }
}
