const container = document.getElementById('data-container');

const updateBooksList = books => {
  if (books.length === 0) {
    container.innerHTML = `
            <div class="empty">
                Книги отсутствуют
            </div>
        `;
  } else {
    container.innerHTML = `
                <ul class="book-list">
                    ${books
                      .map(
                        book => `
                            <a class="book-item" href="/books/${book.id}">
                                <div class="book-title">${book.title}</div>
                                <div class="book-meta">
                                    <span>Автор:
                                        <strong>${book.authorDto.fullName}</strong>
                                    </span>
                                </div>
                                ${
                                  book.genreDtos.length > 0 &&
                                  `
                                        <div class="genres">
                                        ${book.genreDtos.map(genreDto => `<span class="genre">${genreDto.name}</span>`).join('')}
                                        </div>
                                    `
                                }

                            </a>`,
                      )
                      .join('')}
                </ul>
        `;
  }
};

fetch('/api/books')
  .then(response => response.json())
  .then(updateBooksList)
  .catch(err => {
    console.log('error', err);
  });
