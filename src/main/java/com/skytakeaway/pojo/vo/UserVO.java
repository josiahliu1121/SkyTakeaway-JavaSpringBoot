package com.skytakeaway.pojo.vo;

import com.skytakeaway.pojo.entity.AddressBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO implements Serializable {
    private Long id;
    private String userName;
    private LocalDateTime createTime;
    private String emailAddress;
    private String phone;
    private String avatar;
    private List<AddressBook> addressBooks;
}
