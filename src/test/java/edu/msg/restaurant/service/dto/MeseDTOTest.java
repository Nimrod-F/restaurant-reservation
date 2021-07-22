package edu.msg.restaurant.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.msg.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeseDTO.class);
        MeseDTO meseDTO1 = new MeseDTO();
        meseDTO1.setId(1L);
        MeseDTO meseDTO2 = new MeseDTO();
        assertThat(meseDTO1).isNotEqualTo(meseDTO2);
        meseDTO2.setId(meseDTO1.getId());
        assertThat(meseDTO1).isEqualTo(meseDTO2);
        meseDTO2.setId(2L);
        assertThat(meseDTO1).isNotEqualTo(meseDTO2);
        meseDTO1.setId(null);
        assertThat(meseDTO1).isNotEqualTo(meseDTO2);
    }
}
