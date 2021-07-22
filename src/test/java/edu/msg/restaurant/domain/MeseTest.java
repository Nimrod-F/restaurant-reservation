package edu.msg.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.msg.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mese.class);
        Mese mese1 = new Mese();
        mese1.setId(1L);
        Mese mese2 = new Mese();
        mese2.setId(mese1.getId());
        assertThat(mese1).isEqualTo(mese2);
        mese2.setId(2L);
        assertThat(mese1).isNotEqualTo(mese2);
        mese1.setId(null);
        assertThat(mese1).isNotEqualTo(mese2);
    }
}
