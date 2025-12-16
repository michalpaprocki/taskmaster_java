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
import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.entity.Membership;
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
    private OrganizationResponseDTO wayland;

    @BeforeEach
    void setUp() {
        UserRequestDTO dto1 = new UserRequestDTO("jane", "jane@example.com", "My_secr3t_password", null);
        UserRequestDTO dto2 = new UserRequestDTO("frank", "frank@example.com", "My_secr3t_password", null);
        UserRequestDTO dto3 = new UserRequestDTO("cassandra", "cassandra@example.com", "My_secr3t_password", null);
        this.jane = userService.createUser(dto1);
        this.frank = userService.createUser(dto2);
        this.cassandra = userService.createUser(dto3);

        OrganizationRequestDTO orgDto = new OrganizationRequestDTO("Wayland", null);
   
        this.wayland = organizationService.createOrganizationWithOwner(orgDto, jane);
    }

    @Test
    void testCreateOrganization() {
        OrganizationRequestDTO orgDto = new OrganizationRequestDTO("ACME", null);
        OrganizationResponseDTO org = organizationService.createOrganizationWithOwner(orgDto, jane);
        assertThat(org).isNotNull();
        assertThat(org.getName()).isEqualTo("ACME");
        assertThat(org.getMemberships()).filteredOn(m -> m.getRole() == Membership.Role.OWNER).extracting(MemberResponseDTO::getName).contains(jane.getName());
    }

    @Test
    void testIsOwner() {

        assertTrue(organizationService.isOwner(wayland.getId(), jane));
    }

    @Test
    void testGetOrganization() {
        OrganizationResponseDTO org = organizationService.getOrganization(wayland.getId());
        assertThat(org).isNotNull();
        assertThat(org).isInstanceOf(OrganizationResponseDTO.class);
        assertThat(org.getName()).isEqualTo(wayland.getName());
    }
    @Test
    void testGetOrganizationEntity() {
        Organization org = organizationService.getOrganizationEntity(wayland.getId());
        assertThat(org).isNotNull();
        assertThat(org).isInstanceOf(Organization.class);
        assertThat(org.getName()).isEqualTo(wayland.getName());
    }
    
    @Test
    void testAddMember() {
        OrganizationResponseDTO orgDto = organizationService.addMember(wayland.getId(), cassandra);
        assertThat(orgDto.getMemberships()).extracting(MemberResponseDTO::getName).contains(cassandra.getName());
        assertThat(orgDto.getMemberships()).extracting(MemberResponseDTO::getId).doesNotHaveDuplicates();
        assertThat(orgDto.getMemberships()).filteredOn(m -> m.getRole() == Membership.Role.OWNER);
    }
    

    @Test
    void testAddMemberThrows() {
        organizationService.addMember(wayland.getId(), cassandra);
        
        assertThatThrownBy(() -> organizationService.addMember(wayland.getId(), cassandra)).isInstanceOf(IllegalArgumentException.class).hasMessage("User is already a member of this organization");
    }

    @Test
    void testRemoveMember() {
        organizationService.addMember(wayland.getId(), cassandra);
        
        OrganizationResponseDTO org = organizationService.removeMember(wayland.getId(), cassandra);
        assertThat(org.getMemberships()).extracting(MemberResponseDTO::getName).doesNotContain(cassandra.getName());
    }

    @Test
    void testRemoveMemberThrows() {
        assertThatThrownBy(() -> organizationService.removeMember(wayland.getId(), cassandra)).isInstanceOf(IllegalArgumentException.class).hasMessage("User is not a member of this organization");
    }

    @Test
    void testUpdateName() {
        OrganizationRequestDTO dtoReq = new OrganizationRequestDTO("Yutani", null);
        OrganizationResponseDTO dto =  organizationService.updateName(wayland.getId(), dtoReq);
        assertThat(dto.getName()).isEqualTo("Yutani");
    }

}