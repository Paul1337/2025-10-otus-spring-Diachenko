const btnDelete = document.getElementById('btn-delete');
const btnSave = document.getElementById('btn-save');

const form = document.getElementById('form');
const titleEl = form.querySelector('input[name="title"]');
const authorEl = form.querySelector('select[name="authorId"]');
const genresEl = form.querySelector('select[name="genreIds"]');

const bookId = Number(document.body.dataset.bookId);
const Mode = {
    Create: 'create',
    Edit: 'edit'
};
const mode = bookId === 0 ? Mode.Create : Mode.Edit;

const loadReferences = async () => {
    const authorSelect = document.getElementById('author');
    const genresSelect = document.getElementById('genres');

    try {
        const [authorsRes, genresRes] = await Promise.all([
            fetch('/api/authors'),
            fetch('/api/genres')
        ]);

        if (!authorsRes.ok) throw new Error(`Authors HTTP ${authorsRes.status}`);
        if (!genresRes.ok) throw new Error(`Genres HTTP ${genresRes.status}`);

        const authors = await authorsRes.json();
        const genres = await genresRes.json();

        authorSelect.innerHTML = `
            <option value="" disabled selected>
                -- выберите автора --
            </option>
        `;

        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author.id;
            option.textContent = author.fullName;
            authorSelect.append(option);
        });

        genresSelect.innerHTML = '';

        genres.forEach(genre => {
            const option = document.createElement('option');
            option.value = genre.id;
            option.textContent = genre.name;
            genresSelect.append(option);
        });

    } catch (err) {
        console.error('Ошибка загрузки справочников:', err);

        const container = document.querySelector('.container');
        container.innerHTML = `
            <div class="empty">
                Не удалось загрузить данные (авторы/жанры)
            </div>
        `;
    }
};

loadReferences()
.then(() => {
    if (mode === Mode.Edit) {
        const updateBook = (book) => {
            const idInput = document.getElementById('book-id');
            if (idInput) idInput.value = book.id;

            const titleInput = document.getElementById('title');
            if (titleInput) titleInput.value = book.title ?? '';

            const authorSelect = document.getElementById('author');
            if (authorSelect && book.authorDto) {
                authorSelect.value = book.authorDto.id;
            }

            const genresSelect = document.getElementById('genres');
            if (genresSelect && Array.isArray(book.genreDtos)) {
                const genreIds = book.genreDtos.map(g => String(g.id));

                Array.from(genresSelect.options).forEach(opt => {
                    opt.selected = genreIds.includes(opt.value);
                });
            }
        };


        fetch(`/api/books/${bookId}`)
            .then((response) => {
                  if (!response.ok) {
                      if (response.status === 404) {
                          const container = document.querySelector('.container');
                          container.innerHTML = '<div class="empty" style="text-align:center; font-size:16px;">Книга не найдена</div>';
                          return Promise.reject('404 Not Found');
                      }
                      return Promise.reject(`HTTP ${response.status}`);
                  }
                  return response;
            })
            .then(res => res.json())
            .then(updateBook)
            .catch(err => {
                console.log(err);
                // may be 404 error
            })

        btnDelete.addEventListener('click', () => {
            fetch(`/api/books/${bookId}`, {
                method: 'DELETE',
            })
            .then(data => {
                window.location.replace('/books/');
            })
            .catch(err => {
                console.log('error', err);
            })
        })
    }
});



btnSave.addEventListener('click', () => {
    const data = {
        id: bookId != 0 ? bookId : undefined,
        title: titleEl.value,
        authorId: Number(authorEl.value),
        genreIds: Array.from(genresEl.selectedOptions).map(option => Number(option.value))
    };

    const url = mode === Mode.Create ? '/api/books' : `/api/books/${bookId}`;
    fetch(url, {
        method: mode === Mode.Create ? 'POST' : 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        window.location.replace('/books/');
    })
    .catch(err => {
        console.log('error', err);
    })
});

