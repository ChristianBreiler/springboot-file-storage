function togglePasswordVisibility() {
    const passwordField = document.getElementById('passwordField');
    const type = passwordField.type === 'password' ? 'text' : 'password';
    passwordField.type = type;
    const img = document.getElementById("showPasswordIcon");
    img.src = type === 'password' ? "/images/show.png" : "/images/hide.png";
}