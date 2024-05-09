package com.home.balance.api.services

import com.home.balance.api.models.entities.Entry
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.text.SimpleDateFormat

class EntryServiceTest {

    var entryRepository = mock(EntryRepository::class.java)
    var categoryRepository = mock(CategoryRepository::class.java)
    var entryService = EntryService(entryRepository, categoryRepository)

    var argumentCaptor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Entry>>

    @Test
    fun findSplitTest() {

        //  given
        var entries = mutableListOf(
            getEntry("Test (1/2)", "Test (1/2) - compra 01", "01/01/2021", 100.0),
            getEntry("Test (2/2)", null, "01/02/2021", 101.0),

            )

        val expected = listOf(
            getEntry("Test (1/2)", "Test (1/2) *-* compra 01", "01/01/2021", 100.0),
            getEntry("Test (2/2)", "Test (2/2) *-* compra 01", "01/02/2021", 100.0),
        )

//        var entries = mutableListOf(
//            getEntry("Test (1/2)", "Test (1/2) - compra 01", "01/01/2021"),
//            getEntry("Test (3/10)", null, "01/01/2021"),
//            getEntry("Test (4/10)", null, "01/02/2021"),
//            getEntry("Test", null, "01/02/2021"),
//            getEntry("Test (2/2)", null, "01/02/2021"),
//            getEntry("Test (2/10)", null, "01/12/2020"),
//            getEntry("Test (1/10)", "Test (1/10) - compra 02", "01/11/2020"),
//            getEntry("Another Test", null, "01/11/2020"),
//        );
//
//        val expected = listOf(
//            getEntry("Test (1/2)", "Test (1/2) *-* compra 01", "01/01/2021"),
//            getEntry("Test (2/2)", "Test (2/2) *-* compra 01", "01/02/2021"),
//            getEntry("Test (1/10)", "Test (1/10) *-* compra 02", "01/11/2020"),
//            getEntry("Test (2/10)", "Test (2/10) *-* compra 02", "01/12/2020"),
//            getEntry("Test (3/10)", "Test (3/10) *-* compra 02", "01/01/2021"),
//            getEntry("Test (4/10)", "Test (4/10) *-* compra 02", "01/02/2021")
//        )


        //  when
        `when`(entryRepository.findAll()).thenReturn(entries)

        entryService.findSplitPayments()

        verify(entryRepository).saveAll(argumentCaptor.capture())
        val actual = argumentCaptor.value

        //  then
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual)

    }


    fun getEntry(originalDescription: String, description: String?, date: String, originalValue: Double): Entry {
        return Entry(
            originalDescription = originalDescription,
            description = description,
            originalValue = originalValue,
            originalDate = SimpleDateFormat("dd/MM/yyyy").parse(date)
        )

    }


}