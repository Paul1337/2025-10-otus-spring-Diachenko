const btnDelete = document.getElementById('btn-delete');
const btnSave = document.getElementById('btn-save');

const form = document.getElementById('form');
const titleEl = form.querySelector('input[name="title"]');
const authorEl = form.querySelector('select[name="authorId"]');
const genresEl = form.querySelector('select[name="genreIds"]');

const mode = document.body.dataset.mode;
const bookId = Number(document.body.dataset.bookId);

const expectedModes = ['create', 'edit'];
if (!expectedModes.includes(mode)) throw new Error('Unexpected mode');

btnSave.addEventListener('click', () => {
    const data = {
        id: bookId != 0 ? bookId : undefined,
        title: titleEl.value,
        authorId: Number(authorEl.value),
        genreIds: Array.from(genresEl.selectedOptions).map(option => Number(option.value))
    };

    const url = mode === 'create' ? '/books' : `/books/${bookId}`;
    fetch(url, {
        method: mode === 'create' ? 'POST' : 'PUT',
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

btnDelete.addEventListener('click', () => {
    fetch(`/books/${bookId}`, {
        method: 'DELETE',
    })
    .then(data => {
        window.location.replace('/books/');
    })
    .catch(err => {
        console.log('error', err);
    })
})