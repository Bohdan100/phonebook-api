package corp.phonebook.common.dto

import org.springframework.data.domain.Page

data class PageResponse<T : Any>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
) {
    companion object {
        fun <T : Any> from(page: Page<T>): PageResponse<T> {
            return PageResponse(
                content = page.content,
                page = page.number,
                size = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty
            )
        }
    }
}