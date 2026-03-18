const container = document.getElementById('data-container');

const updateAuthorsList = authors => {
    if (authors.length === 0) {
        container.innerHTML = `
                <div class="empty">
                    Нет авторов
                </div>
            `;
    } else {
        container.innerHTML = `
            <ul class="author-list">
                ${
                    authors.map(author => `
                                <li class="author-item">
                                    <span class="author-name">${author.fullName}</span>
                                    <span class="author-id">${author.id}</span>
                                </li>
                    `).join('')
                }
            </ul>
        `;
    }
};

fetch('/api/authors')
  .then(response => response.json())
  .then(updateAuthorsList)
  .catch(err => {
    console.log('error', err);
  });
