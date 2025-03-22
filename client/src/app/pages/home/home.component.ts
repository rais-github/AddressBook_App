import { Component, OnInit } from '@angular/core';
import { AddressBookService } from '../../services/address-book.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  persons: any[] = [];

  constructor(private addressBookService: AddressBookService) {}

  ngOnInit() {
    this.addressBookService.getPersons().subscribe((data) => {
      this.persons = data;
    });
  }
}
