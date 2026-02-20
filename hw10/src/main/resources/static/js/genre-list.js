const container = document.getElementById('data-container');

const updateGenresList = genres => {
    if (genres.length === 0) {
        container.innerHTML = `
                <div class="empty">
                    Нет жанров
                </div>
            `;
    } else {
        container.innerHTML = `
            <ul class="genres-list">
                ${
                    genres.map(genre => `
                                <li class="genre-item">
                                    <span class="genre-name">${genre.name}</span>
                                    <span class="genre-id">${genre.id}</span>
                                </li>
                    `).join('')
                }
            </ul>
        `;
    }
};

fetch('/api/genres')
  .then(response => response.json())
  .then(updateGenresList)
  .catch(err => {
    console.log('error', err);
  });
