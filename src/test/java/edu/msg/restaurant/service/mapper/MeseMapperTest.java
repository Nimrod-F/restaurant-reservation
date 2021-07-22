package edu.msg.restaurant.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeseMapperTest {

    private MeseMapper meseMapper;

    @BeforeEach
    public void setUp() {
        meseMapper = new MeseMapperImpl();
    }
}
