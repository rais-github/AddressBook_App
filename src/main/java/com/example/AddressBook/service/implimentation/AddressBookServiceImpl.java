package com.example.AddressBook.service.implimentation;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.exceptions.AddressBookException;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.repository.AuthUserRepository;
import com.example.AddressBook.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements IAddressBookService {

    @Autowired
    private AddressBookRepository repository;

    @Autowired
    private  MessagePublisher messagePublisher;



    @Autowired
    private AuthUserRepository authUserRepository;  // Inject User Repository

    /**
     * 🔥 Cache the Address Book List
     */
    @Override
    @Cacheable(value = "addressBookCache", key = "'allEntries'")
    public List<AddressBook> getAllEntries() {
        return repository.findAll();
    }

    /**
     * 🔥 Cache a single entry when fetched
     */
    @Override
    @Cacheable(value = "addressBookCache", key = "#id")
    public AddressBook getEntryById(Long id) {
        return repository.findById(id).orElseThrow(() -> new AddressBookException("Entry not found"));
    }

    /**
     * 🔥 Add an entry & Clear the cache
     */

    @CacheEvict(value = "addressBookCache", allEntries = true)
    @Override
    public AddressBook addEntry(AddressBookDTO dto) {
        AddressBook entry = new AddressBook();
        entry.setName(dto.getName());
        entry.setAddress(dto.getAddress());
        entry.setPhoneNumber(dto.getPhoneNumber());

        AuthUser user = authUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AddressBookException("User not found with ID: " + dto.getUserId()));

        entry.setUser(user);
        AddressBook savedEntry = repository.save(entry);

        // 🔥 Publish event for new contact added
        String message = "New contact added: " + entry.getName();
        messagePublisher.sendMessage("address.book.queue", message);

        return savedEntry;
    }

    /**
     * 🔥 Update an entry & Update the cache
     */
    @Override
    @CachePut(value = "addressBookCache", key = "#id")
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        AddressBook entry = getEntryById(id);
        entry.setName(dto.getName());
        entry.setAddress(dto.getAddress());
        entry.setPhoneNumber(dto.getPhoneNumber());

        // 🔥 Fetch and update User
        AuthUser user = authUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AddressBookException("User not found with ID: " + dto.getUserId()));

        entry.setUser(user);
        return repository.save(entry);
    }

    /**
     * 🔥 Delete an entry & Clear cache
     */
    @Override
    @CacheEvict(value = "addressBookCache", key = "#id")
    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }
}