const updateBookItem = book => {
    document.getElementById('book-title').textContent = book.title;
    document.getElementById('book-author').textContent = book.authorDto.fullName;

    const genresContainer = document.getElementById('book-genres-container');

    const genres = book.genreDtos ?? [];

    genresContainer.innerHTML = genres.map(g => `<span class="genre">${g.name}</span>`).join('');
};

const updateComments = comments => {
    const container = document.getElementById('book-comments-container');

    if (comments.length === 0) {
        container.innerHTML = `<div class="empty">Комментариев пока нет</div>`;
    } else {
        container.innerHTML = comments.map(c =>
            `<li class="comment">${c.text}</li>`
        ).join('');
    }
}

const bookId = document.body.dataset.bookId;

fetch(`/api/books/${bookId}`)
  .then(response => {
      if (!response.ok) {
          if (response.status === 404) {
              const container = document.querySelector('.container');
              container.innerHTML = '<div class="empty" style="text-align:center; font-size:16px;">Книга не найдена</div>';
              return Promise.reject('404 Not Found');
          }
          return Promise.reject(`HTTP ${response.status}`);
      }

      return response.json();
  })
  .then(updateBookItem)
  .catch(err => {
      if (err !== '404 Not Found') {
          const errorDiv = document.getElementById('book-error');
          errorDiv.textContent = '! Не удалось загрузить книгу';
          errorDiv.style.display = 'block';
      }
  });

fetch(`/api/books/${bookId}/comments`)
  .then(response => response.json())
  .then(updateComments)
  .catch(err => {
     const errorDiv = document.getElementById('comments-error');
     errorDiv.textContent = '! Не удалось загрузить комментарии';
     errorDiv.style.display = 'block';
  });


