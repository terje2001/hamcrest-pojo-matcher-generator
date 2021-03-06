package ru.yandex.qatools.proc.test;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Vladislav Bauer
 */

public class LordTest {

    @Test
    public void shouldFindMatcherClassForOwner() throws Exception {
        assertThat(Class.forName(LordMatchers.class.getCanonicalName()), notNullValue());
    }

    @Test
    public void shouldFindMatchersForAllField() throws Exception {
        assertThat(
                Stream.of(LordMatchers.class.getDeclaredMethods()).map(Method::getName).collect(toList()), 
                allOf(hasItems("withName", "withSlavesCount"), not(hasItems("withFullName", "withAge")))
        );
        assertThat(LordMatchers.class.getDeclaredMethod("withName", Matcher.class), notNullValue());
        assertThat(LordMatchers.class.getDeclaredMethod("withSlavesCount", Matcher.class), notNullValue());
    }

}
