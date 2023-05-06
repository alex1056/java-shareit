package ru.practicum.shareit.helper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class Helpers {
    public static User composeFieldsForUpdateUser(User user, User usertoUpdate) {
        User userResult = new User();
        userResult.setId(user.getId());
        userResult.setEmail(usertoUpdate.getEmail() != null ? usertoUpdate.getEmail() : user.getEmail());
        userResult.setName(usertoUpdate.getName() != null ? usertoUpdate.getName() : user.getName());
        return userResult;
    }

    public static Item composeFieldsForUpdateItem(Item item, Item itemUpdated) {
        Item itemResult = new Item();
        itemResult.setId(item.getId());
        itemResult.setName(itemUpdated.getName() != null ? itemUpdated.getName() : item.getName());
        itemResult.setDescription(itemUpdated.getDescription() != null ? itemUpdated.getDescription() : item.getDescription());
        itemResult.setAvailable(itemUpdated.isAvailable() != null ? itemUpdated.isAvailable() : item.isAvailable());
        itemResult.setOwnerId(item.getOwnerId());
        itemResult.setRequestId(item.getRequestId());
        return itemResult;
    }

    public static Integer getPageNumber(Integer startIndex, Integer size) {
        Integer result = startIndex / size;
        return result;
    }
}
