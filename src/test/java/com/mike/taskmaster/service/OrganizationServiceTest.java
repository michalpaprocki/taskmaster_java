package com.mike.taskmaster.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mike.taskmaster.dto.MemberResponseDTO;
import com.mike.taskmaster.dto.OrganizationRequestDTO;
import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.dto.OrganizationUpdateDTO;
import com.mike.taskmaster.dto.UserRequestDTO;

import com.mike.taskmaster.entity.Organization;
import com.mike.taskmaster.entity.User;

import jakarta.transaction.Transactional;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class OrganizationServiceTest {
    
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    private User jane;
    private User frank;
    private User cassandra;
    private OrganizationUpdateDTO wayland;

    @BeforeEach
    void setUp() {
        UserRequestDTO dto1 = new UserRequestDTO("jane", "jane@example.com", "my_secr3t_password");
        UserRequestDTO dto2 = new UserRequestDTO("frank", "frank@example.com", "my_secr3t_password");
        UserRequestDTO dto3 = new UserRequestDTO("cassandra", "cassandra@example.com", "my_secr3t_password");
        this.jane = userService.createUser(dto1);
        this.frank = userService.createUser(dto2);
        this.cassandra = userService.createUser(dto3);

        OrganizationRequestDTO orgDto = new OrganizationRequestDTO("Wayland");
   
        Organization org = organizationService.createOrganizationWithOwner(orgDto, jane);
        OrganizationUpdateDTO orgUpdateDto = new OrganizationUpdateDTO(org);
        this.wayland = orgUpdateDto;
    }

    @Test
    void testCreateOrganization() {
        OrganizationRequestDTO orgDto = new OrganizationRequestDTO("ACME");
        Organization org = organizationService.createOrganizationWithOwner(orgDto, jane);
        assertThat(org).isNotNull();
        assertThat(org.getName()).isEqualTo("ACME");
        assertTrue(org.getOwners().stream().anyMatch(m -> m.getUser().equals(jane)));
    }

    @Test
    void testIsOwner() {
        OrganizationUpdateDTO dto = new OrganizationUpdateDTO(wayland.getId());

        assertTrue(organizationService.isOwner(dto, jane));
    }

    @Test
    void testAddMember() {
        OrganizationResponseDTO orgDto = organizationService.addMember(wayland, cassandra);
        assertThat(orgDto.getMemberships()).allMatch(m -> m instanceof MemberResponseDTO);
        assertThat(orgDto.getMemberships()).extracting(m -> m.getName()).contains(cassandra.getName());
    }
    
    @Test
    void testAddMemberThrows() {
        organizationService.addMember(wayland, cassandra);
        
        assertThatThrownBy(() -> organizationService.addMember(wayland, cassandra)).isInstanceOf(IllegalArgumentException.class).hasMessage("User is already a member of this organization");
    }

    @Test
    void testRemoveMember() {
        organizationService.addMember(wayland, cassandra);
        
        Boolean isRemoved = organizationService.removeMember(wayland, cassandra);
        assertTrue(isRemoved);
    }

    @Test
    void testRemoveMemberFails() {
        Boolean isRemoved = organizationService.removeMember(wayland, cassandra);
        assertFalse(isRemoved);
    }

    @Test
    void testUpdateName() {
        OrganizationUpdateDTO dto =  organizationService.updateName(wayland, "Yutani");
        assertThat(dto.getName()).isEqualTo("Yutani");
    }

}