package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Account.AccountResponseDto;
import org.fase2.dwf2.dto.Login.RegisterRequestDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.enums.Role;
import org.fase2.dwf2.enums.UserStatus;
import org.fase2.dwf2.repository.IAccountRepository;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;

    @Autowired
    public UserService(IUserRepository userRepository, IAccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::mapToDto);
    }

    public UserDto searchUserByKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Search key cannot be empty");
        }

        Optional<User> user = userRepository.findByDui(key)
                .or(() -> userRepository.findByEmail(key))
                .or(() -> Optional.ofNullable(accountRepository.findByAccountNumber(key)).map(Account::getUser));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return mapToDto(user.get());
    }


    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUserByEmail(String email, RegisterRequestDto registerRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Field[] fields = RegisterRequestDto.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(registerRequest);
                    if (value != null) {
                        Field userField = User.class.getDeclaredField(field.getName());
                        userField.setAccessible(true);
                        userField.set(user, value);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException("Error updating user field", e);
                }
            }
            User updatedUser = userRepository.save(user);
            return mapToDto(updatedUser);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserProfile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return mapToDto(optionalUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public List<UserDto> getGerenteSucursalUsers() {
        List<User> users = userRepository.findByRole(Role.GERENTE_SUCURSAL);
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDto save(RegisterRequestDto registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setDui(registerRequest.getDui());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());
        user.setStatus(registerRequest.getStatus());

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Transactional(readOnly = true)
    public boolean existsByDui(String dui) {
        return userRepository.existsByDui(dui);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getCajeroUsers() {
        return userRepository.findByRole(Role.CAJERO).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setStatus(UserStatus.DEACTIVATED); // Change status to DEACTIVATED
        userRepository.save(user);
        return mapToDto(user);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDui(user.getDui());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setDui(user.getDui());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());

        // Map associated accounts
        if (user.getAccounts() != null) {
            List<AccountResponseDto> accountDtos = user.getAccounts().stream()
                    .map(this::mapAccountToDto) // Method to map Account to AccountDto
                    .collect(Collectors.toList());
            userDto.setAccounts(accountDtos);
        }

        return userDto;
    }

    public AccountResponseDto mapAccountToDto(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setUserEmail(account.getUser().getEmail());
        dto.setBalance(account.getBalance());
        return dto;
    }

}
